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
        if (els[i].innerText == "") {
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

    if (el.innerText == "")
        return;

    let els = document.getElementsByTagName("button");

    for (let i = 0; i < els.length; ++i) {
        if (els[i].disabled && els[i].innerText == el.innerText) {
            els[i].disabled = false;
            break;
        }
    }

    el.innerText = "";
}