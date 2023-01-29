function up(button) {
    let buttons = document.querySelectorAll(".arrow-up");
    let codes = document.querySelectorAll(".code");

    let selectedIndex = -1;
    for (let i = 0; i < buttons.length; ++i) {
        if (buttons[i] == button) {
            selectedIndex = i;
            break;
        }
    }

    if (selectedIndex <= 0)
        return;

    let code = codes[selectedIndex].innerHTML;
    let previousCode = codes[selectedIndex - 1].innerHTML;

    codes[selectedIndex].innerHTML = previousCode;
    codes[selectedIndex - 1].innerHTML = code;

    let arr = [];

    for (let i = 0; i < codes.length; ++i) {
        arr.push(codes[i].innerHTML);
    }

    Android.sendData(JSON.stringify(arr));
}

function down(button) {
    let buttons = document.querySelectorAll(".arrow-down");
    let codes = document.querySelectorAll(".code");

    let selectedIndex = -1;
    for (let i = 0; i < buttons.length; ++i) {
        if (buttons[i] == button) {
            selectedIndex = i;
            break;
        }
    }

    if (selectedIndex == codes.length - 1)
        return;

    let code = codes[selectedIndex].innerHTML;
    let nextCode = codes[selectedIndex + 1].innerHTML;

    codes[selectedIndex].innerHTML = nextCode;
    codes[selectedIndex + 1].innerHTML = code;

    let arr = [];

    for (let i = 0; i < codes.length; ++i) {
        arr.push(codes[i].innerHTML);
    }

    Android.sendData(JSON.stringify(arr));
}

function process() {
    let arr = [];
    let els = document.querySelectorAll(".answer");

    for (let i = 0; i < els.length; ++i) {
        arr.push(els[i].value);
    }

    Android.sendData(JSON.stringify(arr));
}

function add(el) {

    if (el.disabled)
        return;

    let els = document.getElementsByTagName("span");

    for (let i = 0; i < els.length; ++i) {
        if (els[i].innerText == " ") {
            els[i].innerText = el.innerText;
            el.disabled = true;
            break;
        }
    }

    let arr = [];
    for (let i = 0; i < els.length; ++i)
        arr.push(els[i].innerText);

    Android.sendData(JSON.stringify(arr));
}

function remove(el) {

    if (el.innerText == " ")
        return;

    let els = document.getElementsByTagName("button");

    for (let i = 0; i < els.length; ++i) {
        if (els[i].disabled && els[i].innerText == el.innerText) {
            els[i].disabled = false;
            break;
        }
    }

    el.innerText = " ";
}