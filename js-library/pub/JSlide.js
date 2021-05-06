// We use parameters to create *local* variables in the function, which are faster to lookup than globals, for performance.
// We can also name them something else - like `global` for the window object.
(function(global, document) { 
    function JSlide() {
        // html component
        this.page = null
        this.container = null
        this.slidesBox = null

        // slides info
        this.slides = []
        this.captions = []

        // general state
        this.numSlides = 0
        this.currPageNumber = 0
        this.clickable = false
        this.dblclickable = false
        this.hoverable = false
        this.showPageNumber = false
        this.showcaption = false
        this.reverse = false

        // external button
        this.useButton = false
        this.useDot = false

        // for autoplay
        this.auto = false
        this.speed = 1
        this.pause = false
        this.lock = false
        this.onlyOneAutoShowLock = false

        // animation
        this.animation = false
        this.fade = false
        this.move_horizontal = false
        this.move_vertical = false

        //filter
        this.useFilter = false
        this.blur = false
        this.brightness = false
        this.contrast = false
        this.grayscale = false
        this.hueRotation = false
        this.sepia = false
        this.invert = false
        this.transparent = false
        this.saturate = false

        // class name
        this.slideBoxClass = "slide_box"
        this.slideClass = "slide"
        this.pageNumberClass = "pagenumber"
        this.captionBoxClass = "caption_box"
        this.captionClass = "caption"
        this.slideDisplayClass = "flex"
        this.buttonBoxClass = "button_box"
        this.buttonClass = "my_button"
        this.dotsBoxClass = "dots_class"
        this.dotClass = "my_dot"
        this.dotActiveClass = "dot_active"
        this.myOwnAnimation = null
        this.myOwnFilter = null
    }

    JSlide.prototype = {
        link: function() {
            this.page = document
        },

        selectDisplayArea: function(id, slideBoxClass, slideClass, slideDisplayClass) {
            container = this.page.querySelector(id)
            this.container = container
            this.slidesBox = this.container.getElementsByClassName(slideBoxClass)[0];
            this.slideBoxClass = slideBoxClass
            this.slideClass = slideClass
            this.slideDisplayClass = slideDisplayClass
        },

        buildSlideBox: function() {
            slidesBox = this.page.createElement("div")
            slidesBox.className = this.slideBoxClass
            this.container.appendChild(slidesBox)
            this.slidesBox = slidesBox
        },

        upload_pictures: function(pictures) {
            for (var i = 0; i < pictures.length; i++) {
                pic = this.page.createElement("img")
                pic.className = this.slideClass
                pic.src = pictures[i]
                pic.alt = pictures[i]
                this.slidesBox.appendChild(pic)
            }
        },

        load_pictures: function() {
            if (this.numSlides > 0) {
                this.slides.splice(0, this.numSlides)
                this.numSlides = 0
            }
            
            // slides = this.slidesBox.getElementsByTagName("img")
            // slides = this.slidesBox.querySelectorAll(".slide")
            slides = this.slidesBox.getElementsByClassName(this.slideClass);
            this.numSlides = slides.length
            for (var i = 0; i < this.numSlides; i++) {
                this.slides.push(slides[i])
            }
        },

        addPageNumber: function() {
            if (this.showPageNumber) {
                pageNumberLine = this.page.createElement("div")
                pageNumberLine.className = this.pageNumberClass
                currPageNumber = this.page.createElement("span")
                currPageNumber.innerText = 1
                totalPageNumber = this.page.createElement("span")
                totalPageNumber.innerText = "/" + this.numSlides
                pageNumberLine.appendChild(currPageNumber)
                pageNumberLine.appendChild(totalPageNumber)
                this.slidesBox.insertBefore(pageNumberLine, this.slidesBox.firstChild)
            } else {
                console.log("please ensure the page number is able to be shown")
            }
        },

        addCaption: function(captions) {
            if (this.showcaption) {
                if (captions.length !== this.numSlides) {
                    console.log("number of captions must match number of slides")
                } else {
                    this.captions = captions
                    caption_box = this.page.createElement("div")
                    caption_box.className = this.captionBoxClass
                    caption = this.page.createElement("span")
                    caption.className = this.captionClass
                    caption.innerText = this.captions[0]
                    caption_box.appendChild(caption)
                    this.container.appendChild(caption_box)
                }
            } else {
                console.log("please ensure the caption is able to be shown")
            }
        },

        setAuto: function(ifAuto) {
            this.auto = ifAuto
        },

        setSpeed: function(speed) {
            this.speed = speed
        },

        setReverse: function(reverse) {
            this.reverse = reverse
        },

        setClickable: function(clickable) {
            this.clickable = clickable
        },

        setDblclickable: function(dblclickable) {
            this.dblclickable = dblclickable
        },

        setHoverable: function(hoverable) {
            this.hoverable = hoverable
        },

        setShowPageNumber: function(showPageNumber, pageNumberClass) {
            this.pageNumberClass = pageNumberClass
            this.showPageNumber = showPageNumber
        },

        setShowCaption: function(showCaption, captionBoxClass, captionClass) {
            this.captionBoxClass = captionBoxClass
            this.captionClass = captionClass
            this.showcaption = showCaption
        },

        setButton: function(useButton, buttonBoxClass, buttonClass) {
            this.useButton = useButton
            this.buttonBoxClass = buttonBoxClass
            this.buttonClass = buttonClass
        },

        setDots: function(useDot, dotsBoxClass, dotClass, dotActiveClass) {
            this.useDot = useDot
            this.dotsBoxClass = dotsBoxClass
            this.dotClass = dotClass
            this.dotActiveClass = dotActiveClass
        },

        _animation_event: function(e) {
            e.preventDefault()

            caller = this.caller

            if (caller.fade) {
                caller.slidesBox.style.backgroundColor = 'rgb(210, 229, 247)'
            }
            
            if (caller.move_horizontal || caller.move_vertical) {
                if (!caller.reverse) {
                    caller.slidesBox.style.backgroundImage = "url(" + caller.slides[caller.currPageNumber].src + ")"
                } else {
                    if (caller.currPageNumber == 0) {
                        caller.slidesBox.style.backgroundImage = "url(" + caller.slides[caller.numSlides - 1].src + ")"
                    } else {
                        caller.slidesBox.style.backgroundImage = "url(" + caller.slides[caller.currPageNumber - 1].src + ")"
                    }
                }
            }
        },

        showSlides: function() {
            for (var i = 0; i < this.slides.length; i++) {
                this.slides[i].caller = this
                if (this.animation) {
                    this.slides[i].addEventListener('animationstart', this._animation_event)
                    if (this.fade) {
                        this.slides[i].classList.add("fade")
                    } else if (this.move_horizontal) {
                        if (!this.reverse) {
                            this.slides[i].classList.add("move_horizontal")
                        } else {
                            this.slides[i].classList.add("move_horizontal_reverse")
                        }
                    } else if (this.move_vertical) {
                        if (!this.reverse) {
                            this.slides[i].classList.add("move_vertical")
                        } else {
                            this.slides[i].classList.add("move_vertical_reverse")
                        }
                    } else {
                        this.slides[i].classList.add(this.myOwnAnimation)
                    }
                }

                if (this.useFilter) {
                    if (this.blur) {
                        this.slides[i].classList.add("blur")
                    } else if (this.brightness) {
                        this.slides[i].classList.add("brightness")
                    } else if (this.contrast) {
                        this.slides[i].classList.add("contrast")
                    } else if (this.grayscale) {
                        this.slides[i].classList.add("grayscale")
                    } else if (this.hueRotation) {
                        this.slides[i].classList.add("hue_rotation")
                    } else if (this.sepia) {
                        this.slides[i].classList.add("sepia")
                    } else if (this.invert) {
                        this.slides[i].classList.add("invert")
                    } else if (this.transparent) {
                        this.slides[i].classList.add("transparent")
                    } else if (this.saturate) {
                        this.slides[i].classList.add("saturate")
                    } else {
                        this.slides[i].classList.add(this.myOwnFilter)
                    }
                }
                this.slides[i].style.display = "none";
            }
            this.currPageNumber = 0
            this.slides[0].style.display = this.slideDisplayClass;
        },

        _turnByClick: function(e) {
            e.preventDefault();

            caller = e.target.caller
            let n = 0;
            
            if (this.tagName == "IMG") {
                for (var i = 0; i < caller.slides.length; i++) {
                    if (caller.slides[i].style.display == caller.slideDisplayClass) {
                        if (!caller.reverse) {
                            if (i == caller.slides.length - 1) {
                                n = 0
                            } else {
                                n = i + 1
                            }
                        } else {
                            if (i == 0) {
                                n = caller.slides.length - 1
                            } else {
                                n = i - 1
                            }
                        }
                    }
                    caller.slides[i].style.display = "none";
                }

                if (caller.useDot) {
                    dot_box = caller.container.getElementsByClassName(caller.dotsBoxClass)[0]
                    dots = dot_box.getElementsByClassName(caller.dotClass)

                    for (var i = 0; i < caller.slides.length; i++) {
                        dots[i].classList.remove(caller.dotActiveClass)
                    }
                    dots[n].classList.add(caller.dotActiveClass)
                }
            } else if (this.tagName == "BUTTON" && this.typeName == "non-dot") {
                if (this.job == "prev") {
                    for (var i = 0; i < caller.slides.length; i++) {
                        if (caller.slides[i].style.display == caller.slideDisplayClass) {
                            if (i == 0) {
                                n = caller.slides.length - 1
                            } else {
                                n = i - 1
                            }
                        }
                        caller.slides[i].style.display = "none";
                    }
                } else if (this.job == "next") {
                    for (var i = 0; i < caller.slides.length; i++) {
                        if (caller.slides[i].style.display == caller.slideDisplayClass) {
                            if (i == caller.slides.length - 1) {
                                n = 0
                            } else {
                                n = i + 1
                            }
                        }
                        caller.slides[i].style.display = "none";
                    }
                }

                if (caller.useDot) {
                    dot_box = caller.container.getElementsByClassName(caller.dotsBoxClass)[0]
                    dots = dot_box.getElementsByClassName(caller.dotClass)

                    for (var i = 0; i < caller.slides.length; i++) {
                        dots[i].classList.remove(caller.dotActiveClass)
                    }
                    dots[n].classList.add(caller.dotActiveClass)
                }
            } else if (this.tagName == "BUTTON" && this.typeName == "dot") {
                dot_box = caller.container.getElementsByClassName(caller.dotsBoxClass)[0]
                dots = dot_box.getElementsByClassName(caller.dotClass)

                for (var i = 0; i < caller.slides.length; i++) {
                    if (this.slideNum == i) {
                        n = i
                        this.classList.add(caller.dotActiveClass)
                    } else {
                        dots[i].classList.remove(caller.dotActiveClass)
                    }
                    caller.slides[i].style.display = "none";
                }
            }
        
            caller.currPageNumber = n
            caller.slides[n].style.display = caller.slideDisplayClass;

            if (caller.showPageNumber) {
                pageNumberLine = caller.slidesBox.getElementsByClassName(caller.pageNumberClass)[0];
                currPageNumber = pageNumberLine.firstChild;
                currPageNumber.innerText = n + 1;
            }

            if (caller.showcaption) {
                caption_box = caller.container.getElementsByClassName(caller.captionBoxClass)[0];
                caption = caption_box.getElementsByClassName(caller.captionClass)[0]
                caption.innerText = caller.captions[n]
            }
        },

        showSlidesByClick: function() {
            if (this.auto) {
                console.log("can not work automatically!")
            } else {
                if (this.clickable) {
                    for (var i = 0; i < this.slides.length; i++) {
                        this.slides[i].addEventListener("click", this._turnByClick)
                    }
                } else {
                    console.log("please ensure we read slides by click at first")
                }
            }
        },

        _addButton: function() {
            prev = this.page.createElement("button")
            next = this.page.createElement("button")
            prev.innerText = "last page"
            next.innerText = "next page"
            prev.className= this.buttonClass
            next.className = this.buttonClass
            prev.job = "prev"
            next.job = "next"
            prev.typeName = "non-dot"
            next.typeName= "non-dot"

            button_box = this.page.createElement("div")
            button_box.className = this.buttonBoxClass
            button_box.appendChild(prev)
            button_box.appendChild(next)
            this.container.appendChild(button_box)
            
        },

        showSlidesByButtonClick: function() {
            if (this.auto) {
                console.log("can not work automatically!")
            } else {
                if (this.useButton) {
                    this._addButton()
                    button_box = this.container.getElementsByClassName(this.buttonBoxClass)[0]
                    buttons = button_box.getElementsByClassName(this.buttonClass)

                    for (var i = 0; i < buttons.length; i++) {
                        buttons[i].caller = this
                        buttons[i].addEventListener("click", this._turnByClick)
                    }
                } else {
                    console.log("please ensure the buttons are able to be shown")
                }
            }
        },

        _addDots: function() {
            dots_box = this.page.createElement("div")
            dots_box.className = this.dotsBoxClass
            for (var i = 0; i < this.numSlides; i++) {
                dot = this.page.createElement("button")
                dot.slideNum = i
                dot.typeName = "dot"
                dot.className = this.dotClass

                dots_box.appendChild(dot)
            }

            this.container.appendChild(dots_box)
        },

        _MouseEnterDot: function(e) {
            e.preventDefault();

            caller = e.target.caller
            if (caller.auto) {
                this.hovered = true
                caller.pause = true
            }

            if (caller.currPageNumber != this.slideNum) {
                dot_box = caller.container.getElementsByClassName(caller.dotsBoxClass)[0]
                dots = dot_box.getElementsByClassName(caller.dotClass)
                let n = 0

                for (var i = 0; i < caller.slides.length; i++) {
                    if (this.slideNum == i) {
                        n = i
                    }
                    caller.slides[i].style.display = "none";
                }
                caller.slides[n].style.display = caller.slideDisplayClass;

                if (caller.showPageNumber) {
                    pageNumberLine = caller.slidesBox.getElementsByClassName(caller.pageNumberClass)[0];
                    currPageNumber = pageNumberLine.firstChild;
                    currPageNumber.innerText = n + 1;
                }

                if (caller.showcaption) {
                    caption_box = caller.container.getElementsByClassName(caller.captionBoxClass)[0];
                    caption = caption_box.getElementsByClassName(caller.captionClass)[0]
                    caption.innerText = caller.captions[n]
                }
            }
        },

        _MouseLeaveDot: function(e) {
            e.preventDefault();

            if (caller.auto) {
                this.hovered = false
                caller.pause = false
            }

            caller = e.target.caller
            if (caller.currPageNumber != this.slideNum) {
                dot_box = caller.container.getElementsByClassName(caller.dotsBoxClass)[0]
                dots = dot_box.getElementsByClassName(caller.dotClass)
                let n = 0

                for (var i = 0; i < caller.slides.length; i++) {
                    if (this.slideNum == i) {
                        n = i
                    }
                }
                caller.slides[n].style.display = "none";
                caller.slides[caller.currPageNumber].style.display = caller.slideDisplayClass;

                if (caller.showPageNumber) {
                    pageNumberLine = caller.slidesBox.getElementsByClassName(caller.pageNumberClass)[0];
                    currPageNumber = pageNumberLine.firstChild;
                    currPageNumber.innerText = caller.currPageNumber + 1;
                }

                if (caller.showcaption) {
                    caption_box = caller.container.getElementsByClassName(caller.captionBoxClass)[0];
                    caption = caption_box.getElementsByClassName(caller.captionClass)[0]
                    caption.innerText = caller.captions[caller.currPageNumber]
                }
            }
        },

        showSlidesByDotClick: function() {
            if (this.auto) {
                console.log("can not work automatically!")
            } else {
                if (this.useDot) {
                    this._addDots()
                    dot_box = this.container.getElementsByClassName(this.dotsBoxClass)[0]
                    dots = dot_box.getElementsByClassName(this.dotClass)

                    dots[0].classList.add(this.dotActiveClass)
                    for (var i = 0; i < dots.length; i++) {
                        dots[i].caller = this
                        dots[i].addEventListener("click", this._turnByClick)
                        if (this.hoverable) {
                            dots[i].addEventListener("mouseenter", this._MouseEnterDot)
                            dots[i].addEventListener("mouseleave", this._MouseLeaveDot)
                        }
                    }
                } else {
                    console.log("please ensure the dots buttons is able to be shown")
                }
            }
        },

        setSpeed: function(speed) {
            this.speed = speed
        },

        _turnAutomatically: function(caller) {
            if (!caller.pause) {
                if (caller.reverse) {
                    if (caller.currPageNumber == 0) {
                        caller.currPageNumber = caller.numSlides - 1
                    } else {
                        caller.currPageNumber -= 1
                    }
                } else {
                    if (caller.currPageNumber == caller.numSlides - 1) {
                        caller.currPageNumber = 0
                    } else {
                        caller.currPageNumber += 1
                    }
                }

                for (var i = 0; i < caller.numSlides; i++) {
                    caller.slides[i].style.display = "none"
                }

                caller.currPageNumber = caller.currPageNumber
                caller.slides[caller.currPageNumber].style.display = caller.slideDisplayClass;

                if (caller.useDot) {
                    dot_box = caller.container.getElementsByClassName(caller.dotsBoxClass)[0]
                    dots = dot_box.getElementsByClassName(caller.dotClass)

                    for (var i = 0; i < caller.numSlides; i++) {
                        if (!dots[i].hovered) {
                            dots[i].classList.remove(caller.dotActiveClass)
                        }
                    }
                    dots[caller.currPageNumber].classList.add(caller.dotActiveClass)
                }

                if (caller.showPageNumber) {
                    pageNumberLine = caller.slidesBox.getElementsByClassName(caller.pageNumberClass)[0];
                    currPageNumber = pageNumberLine.firstChild;
                    currPageNumber.innerText = caller.currPageNumber + 1;
                }

                if (caller.showcaption) {
                    caption_box = caller.container.getElementsByClassName(caller.captionBoxClass)[0];
                    caption = caption_box.getElementsByClassName(caller.captionClass)[0]
                    caption.innerText = caller.captions[caller.currPageNumber]
                }
            }
        },

        _MouseEnterAuto: function(e) {
            e.preventDefault()
            if (!this.caller.lock) { this.caller.pause = true }
        },

        _MouseLeaveAuto: function(e) {
            e.preventDefault()
            if (!this.caller.lock) { this.caller.pause = false }
        },

        _MouseClickAuto: function(e) {
            e.preventDefault()
            this.caller.lock = !this.caller.lock
            this.caller.pause = true
        },

        _MouseDblclickAuto: function(e) {
            e.preventDefault()
            this.caller.lock = !this.caller.lock
            this.caller.pause = false
        },

        showSlidesAutomatically: function() {
            if (this.auto) {
                for (var i = 0; i < this.numSlides; i++) {
                    if (this.hoverable) {
                        this.slides[i].addEventListener("mouseenter", this._MouseEnterAuto)
                        this.slides[i].addEventListener("mouseleave", this._MouseLeaveAuto)
                    }
                    if (this.clickable) { this.slides[i].addEventListener("click", this._MouseClickAuto) }
                    if (this.dblclickable) { this.slides[i].addEventListener("dblclick", this._MouseDblclickAuto) }
                }

                if (!this.onlyOneAutoShowLock) {
                    this.onlyOneAutoShowLock = true
                    const caller = this
                    setInterval(caller._turnAutomatically, caller.speed * 1000, caller)
                }
            } else {
                console.log("please ensure we show slides automatically")
            }
        },

        _addButtonAuto: function() {
            stop = this.page.createElement("button")
            not_stop = this.page.createElement("button")
            stop.innerText = "stop"
            not_stop.innerText = "do not stop"
            stop.className= this.buttonClass
            not_stop.className = this.buttonClass
            stop.job = "stop"
            not_stop.job = "not_stop"
            prev.typeName = "non-dot"
            next.typeName= "non-dot"

            button_box = this.page.createElement("div")
            button_box.className = this.buttonBoxClass
            button_box.appendChild(stop)
            button_box.appendChild(not_stop)
            this.container.appendChild(button_box)
            
        },

        _enableButtonInAuto: function(e) {
            e.preventDefault()

            this.caller.lock = !this.caller.lock
            if (this.job == "stop") {
                this.caller.pause = true
            } else if (this.job == "not_stop") {
                this.caller.pause = false
            }
        },

        showSlidesByButtonClickAuto: function() {
            if (this.useButton) {
                this._addButtonAuto()
                button_box = this.container.getElementsByClassName(this.buttonBoxClass)[0]
                buttons = button_box.getElementsByClassName(this.buttonClass)

                for (var i = 0; i < this.numSlides; i++) {
                    if (this.hoverable) {
                        this.slides[i].addEventListener("mouseenter", this._MouseEnterAuto)
                        this.slides[i].addEventListener("mouseleave", this._MouseLeaveAuto)
                    }
                    if (this.clickable) { this.slides[i].addEventListener("click", this._MouseClickAuto) }
                    if (this.dblclickable) { this.slides[i].addEventListener("dblclick", this._MouseDblclickAuto) }
                }

                for (var i = 0; i < buttons.length; i++) {
                    buttons[i].caller = this
                    buttons[i].addEventListener("click", this._enableButtonInAuto)
                }

                if (!this.onlyOneAutoShowLock) {
                    this.onlyOneAutoShowLock = true
                    const caller = this
                    setInterval(caller._turnAutomatically, caller.speed * 1000, caller)
                }
            } else {
                console.log("please ensure the buttons are able to be shown")
            }
        },

        showSlidesByDotAuto: function() {
            if (this.useDot) {
                this._addDots()
                dot_box = this.container.getElementsByClassName(this.dotsBoxClass)[0]
                dots = dot_box.getElementsByClassName(this.dotClass)

                dots[0].classList.add(this.dotActiveClass)
                for (var i = 0; i < dots.length; i++) {
                    dots[i].caller = this
                    dots[i].hovered = false
                    dots[i].addEventListener("click", this._turnByClick)
                    if (this.hoverable) {
                        dots[i].addEventListener("mouseenter", this._MouseEnterDot)
                        dots[i].addEventListener("mouseleave", this._MouseLeaveDot)
                    }
                }
                
                if (!this.onlyOneAutoShowLock) {
                    this.onlyOneAutoShowLock = true
                    const caller = this
                    setInterval(caller._turnAutomatically, caller.speed * 1000, caller)
                }
            } else {
                console.log("please ensure the dots buttons is able to be shown")
            }
        },

        addAnimation: function(animation) {
            this.animation = true
            switch(animation) {
                case "fade":
                    this.fade = true
                    break
                case "move_horizontal":
                    this.move_horizontal = true
                    break
                case "move_vertical":
                    this.move_vertical = true
                    break
                default:
                    this.myOwnAnimation = animation
                    break
            }
        },

        addFilter: function(filter) {
            this.useFilter = true
            switch(filter) {
                case "blur":
                    this.blur = true
                    break
                case "brightness":
                    this.brightness = true
                    break
                case "contrast":
                    this.contrast = true
                    break
                case "grayscale":
                    this.grayscale = true
                    break
                case "hue_rotation":
                    this.hueRotation = true
                    break
                case "sepia":
                    this.sepia = true
                    break
                case "invert":
                    this.invert = true
                    break
                case "transparent":
                    this.transparent = true
                    break
                case "saturate":
                    this.saturate = true
                    break
                default:
                    this.myOwnFilter = filter
                    break
            }
        },

        defaultArgs: function(id, slideBoxClass, slideClass, slideDisplayClass) {
            var args = {
                id: id,
                slideBoxClass: slideBoxClass,
                slideClass: slideClass,
                slideDisplayClass: slideDisplayClass,
                image_path: null,
                showPageNumber: false,
                pageNumberClass: null,
                showCaption: false,
                captions: null,
                captionBoxClass: null,
                captionClass: null,
                clickable: false,
                useButton: false,
                buttonBoxClass: null,
                buttonClass: null,
                hoverable: false,
                useDot: false,
                dotsBoxClass: null,
                dotClass: null,
                dotActiveClass: null,
                auto: false,
                reverse: false,
                dblclickable: false,
                speed: 1,
                animation: null,
                filter: null
            }

            return args
        },

        easySetup: function(args) {
            this.link()
            this.selectDisplayArea(args.id, args.slideBoxClass, args.slideClass, args.slideDisplayClass)

            if (args.image_path) {
                this.buildSlideBox()
                this.upload_pictures(args.image_path)
            }
            this.load_pictures()

            this.setAuto(args.auto) // if display automatically
            this.setReverse(args.reverse) // show slides reversely

            // page number and captions
            this.setShowPageNumber(args.showPageNumber, args.pageNumberClass)
            this.addPageNumber()
            this.setShowCaption(args.showCaption, args.captionBoxClass, args.captionClass)
            this.addCaption(args.captions)

            // gesture
            this.setClickable(args.clickable) // click
            this.setHoverable(args.hoverable) // hoverable
            if (args.auto) {
                this.setDblclickable(args.dblclickable) // double click
            }

            // display method
            this.setButton(args.useButton, args.buttonBoxClass, args.buttonClass) // buttons
            this.setDots(args.useDot, args.dotsBoxClass, args.dotClass, args.dotActiveClass) // dots

            // add animation
            if (args.animation) {
                this.addAnimation(args.animation)
            }

            // add filter
            if (args.filter) {
                this.addFilter(args.filter)
            }

            this.showSlides() //show slides
            if (args.auto) { // shows slides automatically
                this.setSpeed(args.speed) // transition speed for each slide
                this.showSlidesAutomatically()
                if (args.useButton) { // enable buttons for automatical slideshow
                    this.showSlidesByButtonClickAuto()
                }
                if (args.useDot) { // enable dots for automatical slideshow
                    this.showSlidesByDotAuto()
                }
            } else { // show slides manually
                if (args.clickable) { // click to turn over the page
                    this.showSlidesByClick()
                }
                if (args.useButton) { // use button to turn over the page
                    this.showSlidesByButtonClick()
                }
                if (args.useDot) { // use dot to turn over the page
                    this.showSlidesByDotClick()
                }
            }
        },

    }
    global.JSlide = global.JSlide || JSlide
})(window, window.document); // pass the global window object and jquery to the anonymous function. They will now be locally scoped inside of the function.

// With reference to:
// https://www.w3schools.com/howto/howto_js_slideshow.asp (How TO Create a Slideshow)
// https://www.w3schools.com/w3css/w3css_slideshow.asp (W3.CSS Slideshow)
