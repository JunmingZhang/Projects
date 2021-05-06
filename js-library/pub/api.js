backButton = document.querySelector("#back")
backButton.addEventListener('click', () => {
    location.href='./examples.html'
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

const enableEasyGuideButtons = function() {

    document.querySelector("#defaultArgs").code_active = true
    document.querySelector("#easySetup").code_active = false
    
    document.querySelector("#defaultArgs_button").addEventListener('click', function() {
        document.querySelector("#defaultArgs").classList.remove("code_hidden")
        document.querySelector("#easySetup").classList.add("code_hidden")

        document.querySelector("#defaultArgs").code_active = true
        document.querySelector("#easySetup").code_active = false
    })

    document.querySelector("#defaultArgs_button").addEventListener('mouseenter', function() {
        if (!document.querySelector("#defaultArgs").code_active) {
            document.querySelector("#defaultArgs").classList.remove("code_hidden")
            document.querySelector("#easySetup").classList.add("code_hidden")
        }
    })

    document.querySelector("#defaultArgs_button").addEventListener('mouseleave', function() {
        if (!document.querySelector("#defaultArgs").code_active) {
            document.querySelector("#defaultArgs").classList.add("code_hidden")
        }
        if (document.querySelector("#easySetup").code_active) {
            document.querySelector("#easySetup").classList.remove("code_hidden")
        }
    })

    document.querySelector("#easySetup_button").addEventListener('click', function() {
        document.querySelector("#defaultArgs").classList.add("code_hidden")
        document.querySelector("#easySetup").classList.remove("code_hidden")

        document.querySelector("#defaultArgs").code_active = false
        document.querySelector("#easySetup").code_active = true
    })

    document.querySelector("#easySetup_button").addEventListener('mouseenter', function() {
        if (!document.querySelector("#easySetup").code_active) {
            document.querySelector("#defaultArgs").classList.add("code_hidden")
            document.querySelector("#easySetup").classList.remove("code_hidden")
        }
    })

    document.querySelector("#easySetup_button").addEventListener('mouseleave', function() {
        if (!document.querySelector("#easySetup").code_active) {
            document.querySelector("#easySetup").classList.add("code_hidden")
        }
        if (document.querySelector("#defaultArgs").code_active) {
            document.querySelector("#defaultArgs").classList.remove("code_hidden")
        }
    })
}

enableEasyGuideButtons()
