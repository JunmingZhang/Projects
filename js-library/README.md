# js-library-JSlide

## Introduction
This front-end library helps to play slides (for example, course slides) on the webpage, slides can be played automatically with some animations or manually like PowerPoint. With this library, the user can play slides online instead of downloading them and read it locally, they can choose to play slides manually, so they switch slides by clicking the slide, or they can play it automatically like watching a video, they can choose different playing speed and different animations (like horizontal scrolling, vertical transition, fading), and pause automatic transition by hovering, and other fancy actions or additional features help you read slides. For developers, if they want to design a box for playing slides with any possible area, they can import this library to support playing slides in their box. There is no universal way to design many aspects of slide playing, for example, slides animation. With this library, when a developer needs to realize similar functionality, they do not have to spend lots of time on designing slides playing algorithms and write their code. For example, the way to realize fading can be entirely different from scrolling. My library can be different from others because it supports many animations, playing speed, and automatically playing for slides. For instance, more and more instructors choose to post slides and notes online, but most lecture slides posted online can only be switched by scrolled or clicked manually in the browser. With this library, slides can be played online as PowerPoints developed by Microsoft.

In this library, you can use the default properties to build up the framework of the slide, and also use the default animations and filters to show slides. You can also write your properties to play the slides and extend the library. By designing your settings, please code them in a separate CSS file.

## Features
- Start action: Make slide box and upload images, so the developer does not have to hard-code in the HTML file. 
- Set state, the developer can set the state of the slides to enable or disable some functionalities, or adjust the playing state, or link to some important CSS styles.
- Two ways to play the slideshow: the user can either play the slides manually or automatically. There are different ways to play slides manually, including clicking the page directly, clicking buttons, and clicking dots, I will introduce the last two later.
- Order of playing: the user can play slides either forward or backward.
- Button guidance to play the slides: for playing manually, the developer can either add two buttons so the user can click either button to play the slides back and forth, or add a set of dots, so the user can jump to a page by clicking a dot, and if the developer enables hovering here, the user can hover at the dot to preview a slide. For playing slides automatically, the developer can either add two buttons so the user can click either button to pause the slides permanently (until the user unlocks the slide) or play the slides permanently (until the user locks the slide). And the dots for automatic playing have the same functionality as playing manually.
- Gesture: a set of events to play the slides. Include:
  - Click: if being enabled, for manually playing, the user can click the slide to move to the next or previous page. For automatically playing, the user can click the slide to lock the slide (so not pause the slides) until clicking or double-clicking the slide.
  - Double click: only used in automatically playing, if being enabled, the user can double click the slide to unlock the slide permanently (so the user cannot pause the slide even if hover on the slide), the user can delete the state permanently unlocking by clicking, or double-clicking another time.
  - Hover: if this is enabled, for using dots to show slides, the user can hover at one dot to preview a page, and the slide show will be paused. For automatically playing, the user can hover on the page to pause playing. Hover can pause the page, but not lock or unlock the page.
- Animation: there are different animations for the user to transit between pages, my implemented animations are in the file JSlide_animation.css, the developer can also choose to implement and add his animation by implementing  a new CSS animation style in a CSS file.
- Filter: there are different filters for slides, which provide different views, my implemented filters are in the file JSlide_filter.css, the developer can also choose to implement and add his filter by implementing a new CSS filter style in a CSS file.
- Caption and page number: the developer can show the caption and page number for each slide for users to read more conveniently.

## Relative links
- landing page: https://powerful-shelf-75608.herokuapp.com
- API documentation: https://powerful-shelf-75608.herokuapp.com/api.html

## Library components
- JSlide.js: function implementations of the library, you can import the functions in this library for actions of building the slideshow.
- JSlide_properties.css: the default properties and settings of the slide show built with this library.
- JS_animation.css: the defualt animations of the slide show in this library.
- JS_filter: the defualt filters of the slide show in this library.

## Arguments of the library
Here we talk about the arguments used in the library. I use the body of the function `defaultArgs` as example.
```javascript
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
```

## Function executions
Here we talk about the order of library methods executions. I use the body of the function `easySetup` as example.
```javascript
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
```

## Getting started
In this section, I will show how to start using my library to make your online slides. Since example 1 on the landing page is the easiest to set up, and most friendly to beginners, I will use the example 1 code snippet for beginners to get started.

### Scripts to include
The external scripts the programmer should include in the HTML file, those include the library functions (javascript) and default properties (CSS) used in this library.
```html
<link rel="stylesheet" type="text/css" href="examples.css">
<link rel="stylesheet" type="text/css" href="JSlide_properties.css">
<link rel="stylesheet" type="text/css" href="JSlide_animation.css">
<link rel="stylesheet" type="text/css" href="JSlide_filter.css">

<script type="text/javascript" src='JSlide.js'></script>
```

### Raw code
If you want to use the library with raw methods, you need to care about the order of library method execution, but you can combine mehods more flexibly.
- HTML code snippet:
```html
<div class="slide_frame" id="first_example">
         <div class="slide_box">
            <img src="pictures/xi_an.jpg" alt="xi'an.jpg" class="slide">
            <img src="pictures/beijing.jpg" alt="beijing.jpg" class="slide">
            <img src="pictures/nanjing.jpg" alt="nanjing.jpg" class="slide">
            <img src="pictures/hangzhou.jpg" alt="hangzhou.jpg" class="slide">
            <img src="pictures/wuhan.jpg" alt="wuhan.jpg" class="slide">
            <img src="pictures/wuhan2.jpg" alt="wuhan2.jpg" class="slide">
            <img src="pictures/luoyang.jpg" alt="luoyang.jpg" class="slide">
            <img src="pictures/dunhuang.jpg" alt="dunhuang.jpg" class="slide">
        </div>
</div>
```

- CSS code snippet:
```css
.slide_frame {
   margin: 50px;
}

.slide_box {
   margin: auto;
   width: 800px;
   height: 600px;
   overflow: hidden;
   background-size: 800px 600px;
   background-color: rgb(210, 229, 247);
} 

.slide {
   background-color: aliceblue;
   height: 600px;
   width: 800px;
}

.pagenumber {
   display: block;
   position: absolute;
   color: black;
   font-size: 15px;
   font-weight: bold;
   padding: 12px;
   margin: 0px;
   z-index: 10;
}

.caption_box {
   display: flex;
   flex: auto;
   margin: 10px 50%;
   text-align: center;
}

.caption {
   color: black;
   font-size: 20px;
   font-weight: bold;
   font-family: New Century Schoolbook, TeX Gyre Schola, serif;
}
```

- JavaScript code snippet:
```javascript
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
```

### Use the guidance function "easySetup"
If you want to use the function "easySetup", you only have to consider setting arguments, but not the order of method execution. The CSS and the HTML code snippets are the same as above. In this subsection, we use the methods `defaultArgs` and `easySetup` to generate the example above.

```javascript
test = new JSlide()
args = test.defaultArgs("#easySetup", "slide_box", "slide", "flex")
args.clickable = true
args.showCaption = true
args.showPageNumber = true
args.pageNumberClass = "pagenumber"
args.captions = captions
args.captionBoxClass = "caption_box"
args.captionClass = "caption"

test.easySetup(args)
```
