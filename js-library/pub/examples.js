const log = console.log
const captions = ["Xi'an", "Beijing", "Nanjing", "Hangzhou", "Wuhan", "Wuhan2", "Luoyang", "Dunhuang"]
const image_path = ["pictures/xi_an.jpg", "pictures/beijing.jpg",
"pictures/nanjing.jpg", "pictures/hangzhou.jpg", "pictures/wuhan.jpg", "pictures/wuhan2.jpg", "pictures/luoyang.jpg", "pictures/dunhuang.jpg"]

apiButton = document.querySelector("#api")
repoButton = document.querySelector("#repo")
apiButton.addEventListener('click', function() {
    location.href='./api.html'
})
repoButton.addEventListener('click', function() {
    location.href='https://github.com/csc309-winter-2021/js-library-zhan4662.git'
})

const enableExampleButtons = function(exp) {
    // add string format
    // from https://stackoverflow.com/questions/610406/javascript-equivalent-to-printf-string-format
    if (!String.prototype.format) {
        String.prototype.format = function() {
          var args = arguments;
          return this.replace(/{(\d+)}/g, function(match, number) { 
            return typeof args[number] != 'undefined'
              ? args[number]
              : match
            ;
          });
        };
    }

    document.querySelector("#{0}_html".format(exp)).code_active = true
    document.querySelector("#{0}_css".format(exp)).code_active = false
    document.querySelector("#{0}_js".format(exp)).code_active = false
    
    document.querySelector("#{0}_html_button".format(exp)).addEventListener('click', function() {
        document.querySelector("#{0}_html".format(exp)).classList.remove("code_hidden")
        document.querySelector("#{0}_css".format(exp)).classList.add("code_hidden")
        document.querySelector("#{0}_js".format(exp)).classList.add("code_hidden")

        document.querySelector("#{0}_html".format(exp)).code_active = true
        document.querySelector("#{0}_css".format(exp)).code_active = false
        document.querySelector("#{0}_js".format(exp)).code_active = false
    })

    document.querySelector("#{0}_html_button".format(exp)).addEventListener('mouseenter', function() {
        if (!document.querySelector("#{0}_html".format(exp)).code_active) {
            document.querySelector("#{0}_html".format(exp)).classList.remove("code_hidden")
            document.querySelector("#{0}_css".format(exp)).classList.add("code_hidden")
            document.querySelector("#{0}_js".format(exp)).classList.add("code_hidden")
        }
    })

    document.querySelector("#{0}_html_button".format(exp)).addEventListener('mouseleave', function() {
        if (!document.querySelector("#{0}_html".format(exp)).code_active) {
            document.querySelector("#{0}_html".format(exp)).classList.add("code_hidden")
        }
        if (document.querySelector("#{0}_css".format(exp)).code_active) {
            document.querySelector("#{0}_css".format(exp)).classList.remove("code_hidden")
        }
        if (document.querySelector("#{0}_js".format(exp)).code_active) {
            document.querySelector("#{0}_js".format(exp)).classList.remove("code_hidden")
        }
    })

    document.querySelector("#{0}_css_button".format(exp)).addEventListener('click', function() {
        document.querySelector("#{0}_html".format(exp)).classList.add("code_hidden")
        document.querySelector("#{0}_css".format(exp)).classList.remove("code_hidden")
        document.querySelector("#{0}_js".format(exp)).classList.add("code_hidden")

        document.querySelector("#{0}_html".format(exp)).code_active = false
        document.querySelector("#{0}_css".format(exp)).code_active = true
        document.querySelector("#{0}_js".format(exp)).code_active = false
    })

    document.querySelector("#{0}_css_button".format(exp)).addEventListener('mouseenter', function() {
        if (!document.querySelector("#{0}_css".format(exp)).code_active) {
            document.querySelector("#{0}_html".format(exp)).classList.add("code_hidden")
            document.querySelector("#{0}_css".format(exp)).classList.remove("code_hidden")
            document.querySelector("#{0}_js".format(exp)).classList.add("code_hidden")
        }
    })

    document.querySelector("#{0}_css_button".format(exp)).addEventListener('mouseleave', function() {
        if (!document.querySelector("#{0}_css".format(exp)).code_active) {
            document.querySelector("#{0}_css".format(exp)).classList.add("code_hidden")
        }
        if (document.querySelector("#{0}_html".format(exp)).code_active) {
            document.querySelector("#{0}_html".format(exp)).classList.remove("code_hidden")
        }
        if (document.querySelector("#{0}_js".format(exp)).code_active) {
            document.querySelector("#{0}_js".format(exp)).classList.remove("code_hidden")
        }
    })

    document.querySelector("#{0}_js_button".format(exp)).addEventListener('click', function() {
        document.querySelector("#{0}_html".format(exp)).classList.add("code_hidden")
        document.querySelector("#{0}_css".format(exp)).classList.add("code_hidden")
        document.querySelector("#{0}_js".format(exp)).classList.remove("code_hidden")

        document.querySelector("#{0}_html".format(exp)).code_active = false
        document.querySelector("#{0}_css".format(exp)).code_active = false
        document.querySelector("#{0}_js".format(exp)).code_active = true
    })

    document.querySelector("#{0}_js_button".format(exp)).addEventListener('mouseenter', function() {
        if (!document.querySelector("#{0}_js".format(exp)).code_active) {
            document.querySelector("#{0}_html".format(exp)).classList.add("code_hidden")
            document.querySelector("#{0}_css".format(exp)).classList.add("code_hidden")
            document.querySelector("#{0}_js".format(exp)).classList.remove("code_hidden")
        }
    })

    document.querySelector("#{0}_js_button".format(exp)).addEventListener('mouseleave', function() {
        if (!document.querySelector("#{0}_js".format(exp)).code_active) {
            document.querySelector("#{0}_js".format(exp)).classList.add("code_hidden")
        }
        if (document.querySelector("#{0}_html".format(exp)).code_active) {
            document.querySelector("#{0}_html".format(exp)).classList.remove("code_hidden")
        }
        if (document.querySelector("#{0}_css".format(exp)).code_active) {
            document.querySelector("#{0}_css".format(exp)).classList.remove("code_hidden")
        }
    })
}

enableExampleButtons("ex1")
enableExampleButtons("ex2")
enableExampleButtons("ex3")
enableExampleButtons("ex4")
enableExampleButtons("ex5")
enableExampleButtons("ex6")
enableExampleButtons("ex78")
enableExampleButtons("ex910")
enableExampleButtons("ex1112")
enableExampleButtons("ex1314")
enableExampleButtons("ex1516")
enableExampleButtons("ex_easySetup")

example1 = new JSlide()
example1.link()
example1.selectDisplayArea("#first_example", "slide_box", "slide", "flex")
example1.load_pictures()
example1.showSlides()
example1.setShowPageNumber(true, "pagenumber")
example1.setShowCaption(true, "caption_box", "caption")
example1.setClickable(true)
example1.addCaption(captions)
example1.addPageNumber()
example1.showSlidesByClick()

example2 = new JSlide()
example2.link()
example2.selectDisplayArea("#second_example", "slide_box", "slide", "flex")
example2.buildSlideBox()
example2.upload_pictures(image_path)
example2.load_pictures()
example2.setClickable(true)
example2.setShowCaption(true, "caption_box", "caption")
example2.setButton(true, "button_box", "my_button")
example2.showSlides()
example2.showSlidesByClick()
example2.addCaption(captions)
example2.showSlidesByButtonClick()

example3 = new JSlide()
example3.link()
example3.selectDisplayArea("#third_example", "slide_box", "slide", "flex")
example3.buildSlideBox()
example3.upload_pictures(image_path)
example3.load_pictures()
example3.setShowCaption(true, "caption_box", "caption")
example3.setShowPageNumber(true, "pagenumber")
example3.setHoverable(true)
example3.setDots(true, "dots_box", "my_dot", "dot_active")
example3.setClickable(true)
example3.showSlides()
example3.addCaption(captions)
example3.addPageNumber()
example3.showSlidesByDotClick()
example3.showSlidesByClick()

example4 = new JSlide()
example4.link()
example4.selectDisplayArea("#fourth_example", "slide_box", "slide", "flex")
example4.buildSlideBox()
example4.upload_pictures(image_path)
example4.load_pictures()
example4.setAuto(true)
example4.setSpeed(2)
example4.setHoverable(true)
example4.setClickable(true)
example4.setDblclickable(true)
example4.setShowCaption(true, "caption_box", "caption")
example4.setShowPageNumber(true, "pagenumber")
example4.showSlides()
example4.addCaption(captions)
example4.addPageNumber()
example4.showSlidesAutomatically()

example5 = new JSlide()
example5.link()
example5.selectDisplayArea("#fifth_example", "slide_box", "slide", "flex")
example5.buildSlideBox()
example5.upload_pictures(image_path)
example5.load_pictures()
example5.setShowCaption(true, "caption_box", "caption")
example5.setShowPageNumber(true, "pagenumber")
example5.setAuto(true)
example5.setReverse(true)
example5.setHoverable(true)
example5.setSpeed(1.5)
example5.setButton(true, "button_box", "my_button")
example5.showSlides()
example5.addCaption(captions)
example5.addPageNumber()
example5.showSlidesAutomatically()
example5.showSlidesByButtonClickAuto()

example6 = new JSlide()
example6.link()
example6.selectDisplayArea("#sixth_example", "slide_box", "slide", "flex")
example6.buildSlideBox()
example6.upload_pictures(image_path)
example6.load_pictures()
example6.setShowCaption(true, "caption_box", "caption")
example6.setShowPageNumber(true, "pagenumber")
example6.setAuto(true)
example6.setHoverable(true)
example6.setDots(true, "dots_box", "my_dot", "dot_active")
example6.setClickable(true)
example6.setDblclickable(true)
example6.showSlides()
example6.addCaption(captions)
example6.addPageNumber()
example6.showSlidesByDotAuto()
example6.showSlidesAutomatically()

example7 = new JSlide()
example7.link()
example7.selectDisplayArea("#seventh_example", "slide_box", "slide", "flex")
example7.buildSlideBox()
example7.upload_pictures(image_path)
example7.load_pictures()
example7.addAnimation("fade")
example7.setClickable(true)
example7.setShowCaption(true, "caption_box", "caption")
example7.addCaption(captions)
example7.setButton(true, "button_box", "my_button")
example7.showSlides()
example7.showSlidesByClick()
example7.showSlidesByButtonClick()

example8 = new JSlide()
example8.link()
example8.selectDisplayArea("#eighth_example", "slide_box", "slide", "flex")
example8.buildSlideBox()
example8.upload_pictures(image_path)
example8.load_pictures()
example8.setAuto(true)
example8.setSpeed(2)
example8.setShowPageNumber(true, "pagenumber")
example8.addPageNumber()
example8.setHoverable(true)
example8.setClickable(true)
example8.setDblclickable(true)
example8.setShowCaption(true, "caption_box", "caption")
example8.addAnimation("fade")
example8.showSlides()
example8.addCaption(captions)
example8.showSlidesAutomatically()

example9 = new JSlide()
example9.link()
example9.selectDisplayArea("#ninth_example", "slide_box", "slide", "flex")
example9.buildSlideBox()
example9.upload_pictures(image_path)
example9.load_pictures()
example9.setAuto(true)
example9.setSpeed(2)
example9.setReverse(true)
example9.setShowPageNumber(true, "pagenumber")
example9.setHoverable(true)
example9.setClickable(true)
example9.setDblclickable(true)
example9.setShowCaption(true, "caption_box", "caption")
example9.setDots(true, "dots_box", "my_dot", "dot_active")
example9.setButton(true, "button_box", "my_button")
example9.addAnimation("move_horizontal")
example9.showSlides()
example9.addPageNumber()
example9.addCaption(captions)
example9.showSlidesAutomatically()
example9.showSlidesByDotAuto()
example9.showSlidesByButtonClickAuto()

example10 = new JSlide()
example10.link()
example10.selectDisplayArea("#tenth_example", "slide_box", "slide", "flex")
example10.buildSlideBox()
example10.upload_pictures(image_path)
example10.load_pictures()
example10.setShowCaption(true, "caption_box", "caption")
example10.setHoverable(true)
example10.setClickable(true)
example10.setDots(true, "dots_box", "my_dot", "dot_active")
example10.setShowPageNumber(true, "pagenumber")
example10.addAnimation("move_horizontal")
example10.showSlides()
example10.addPageNumber()
example10.addCaption(captions)
example10.showSlidesByClick()
example10.showSlidesByDotClick()

example11 = new JSlide()
example11.link()
example11.selectDisplayArea("#eleventh_example", "slide_box", "slide", "flex")
example11.buildSlideBox()
example11.upload_pictures(image_path)
example11.load_pictures()
example11.setAuto(true)
example11.setSpeed(2)
example11.setReverse(true)
example11.setHoverable(true)
example11.setClickable(true)
example11.setDblclickable(true)
example11.setShowPageNumber(true, "pagenumber")
example11.setShowCaption(true, "caption_box", "caption")
example11.addAnimation("move_vertical")
example11.showSlides()
example11.addCaption(captions)
example11.addPageNumber()
example11.showSlidesAutomatically()

example12 = new JSlide()
example12.link()
example12.selectDisplayArea("#twelfth_example", "slide_box", "slide", "flex")
example12.buildSlideBox()
example12.upload_pictures(image_path)
example12.load_pictures()
example12.setClickable(true)
example12.setShowPageNumber(true, "pagenumber")
example12.setShowCaption(true, "caption_box", "caption")
example12.setHoverable(true)
example12.addAnimation("move_vertical")
example12.showSlides()
example12.addPageNumber()
example12.addCaption(captions)
example12.showSlidesByClick()

example13 = new JSlide()
example13.link()
example13.selectDisplayArea("#thirteenth_example", "slide_box", "slide", "flex")
example13.buildSlideBox()
example13.upload_pictures(image_path)
example13.load_pictures()
example13.setReverse(true)
example13.setAuto(true)
example13.setSpeed(4)
example13.setHoverable(true)
example13.setClickable(true)
example13.setDblclickable(true)
example13.setShowPageNumber(true, "pagenumber")
example13.setShowCaption(true, "caption_box", "caption")
example13.addAnimation("fancy_fade")
example13.showSlides()
example13.addCaption(captions)
example13.addPageNumber()
example13.showSlidesAutomatically()

example14 = new JSlide()
example14.link()
example14.selectDisplayArea("#fourteenth_example", "slide_box", "slide", "flex")
example14.buildSlideBox()
example14.upload_pictures(image_path)
example14.load_pictures()
example14.setClickable(true)
example14.setShowPageNumber(true, "pagenumber")
example14.setShowCaption(true, "caption_box", "caption")
example14.setHoverable(true)
example14.addAnimation("fancy_fade")
example14.showSlides()
example14.addPageNumber()
example14.addCaption(captions)
example14.showSlidesByClick()

example15 = new JSlide()
example15.link()
example15.selectDisplayArea("#fifteenth_example", "slide_box", "slide", "flex")
example15.buildSlideBox()
example15.upload_pictures(image_path)
example15.load_pictures()
example15.setClickable(true)
example15.setShowCaption(true, "caption_box", "caption")
example15.setButton(true, "button_box", "my_button")
example15.setShowPageNumber(true, "pagenumber")
example15.addFilter("grayscale")
example15.showSlides()
example15.showSlidesByClick()
example15.addCaption(captions)
example15.addPageNumber()
example15.showSlidesByButtonClick()

example16 = new JSlide()
example16.link()
example16.selectDisplayArea("#sixteenth_example", "slide_box", "slide", "flex")
example16.buildSlideBox()
example16.upload_pictures(image_path)
example16.load_pictures()
example16.setAuto(true)
example16.setSpeed(2)
example16.setHoverable(true)
example16.setClickable(true)
example16.setDblclickable(true)
example16.setShowCaption(true, "caption_box", "caption")
example16.setShowPageNumber(true, "pagenumber")
example16.addFilter("my_sepia")
example16.showSlides()
example16.addCaption(captions)
example16.addPageNumber()
example16.showSlidesAutomatically()

test = new JSlide()
args = test.defaultArgs("#easySetup", "slide_box", "slide", "flex")
args.image_path = image_path
args.clickable = true
args.showCaption = true
args.showPageNumber = true
args.pageNumberClass = "pagenumber"
args.captions = captions
args.captionBoxClass = "caption_box"
args.captionClass = "caption"

test.easySetup(args)
