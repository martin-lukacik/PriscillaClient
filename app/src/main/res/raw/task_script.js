function loadData(data) {
    let el = document.querySelector("#task-content");
    el.innerHTML = data;
}

// TASK_ORDER

function loadTaskOrder(content, json) {
    let arr = JSON.parse(json);

    content += "<hr><style>pre{display:inline-block;vertical-align:middle}</style>";
    content += "<div class=\"codes\">";
    for (let i = 0; i < arr.length; ++i) {
        content += "<span><button onclick=\"up(this)\" class=\"arrow-up\">&uarr;</button><button onclick=\"down(this)\" class=\"arrow-down\">&darr;</button><span class=\"code\">";
        content += arr[i];
        content += "</span><br></span>";
    }
    content += "</div>";

    loadData(content);
    Android.sendData(json);
}

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

// TASK_FILL

function loadTaskFill(json) {
    let arr = JSON.parse(json);
    let els = document.querySelectorAll(".answer");

    for (let i = 0; i < els.length; ++i) {
        els[i].value = arr[i];
    }

    process();
}

function process() {
    let arr = [];
    let els = document.querySelectorAll(".answer");

    for (let i = 0; i < els.length; ++i) {
        arr.push(els[i].value);
    }

    Android.sendData(JSON.stringify(arr));
}

// TASK_DRAG

function loadTaskDrag(json) {
    let arr = JSON.parse(json);
    let els = document.getElementsByTagName("span");

    for (let i = 0; i < els.length; ++i) {
        els[i].innerText = arr[i];
    }

    els = document.getElementsByTagName("button");
    for (let i = 0; i < els.length; ++i) {
        els[i].disabled = false;
        for (let j = arr.length - 1; j >= 0; --j) {
            if (els[i].innerText == arr[j]) {
                els[i].disabled = true;
                arr.splice(0, 1);
                break;
            }
        }
    }

    Android.sendData(json);
}

function collectDrag() {
    let els = document.getElementsByTagName("span");
    let arr = [];
    for (let i = 0; i < els.length; ++i)
        arr.push(els[i].innerText);

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

    collectDrag();
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

    collectDrag();
}