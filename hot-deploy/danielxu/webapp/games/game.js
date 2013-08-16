var CODES = "QWERTYUIOPASDFGHJKLZXCVBNM";

// 参数代表最大的值,不包含最大值
function getRandom(maxValue) {
	return parseInt(Math.random() * maxValue);
}

// 获取需要创建的label的字母.
function getLabelCode() {
	var index = getRandom(26);
	var code = CODES.charAt(index);
	return code;
}
var colorchange = "ABCDEF0123456789";

// 创建label标签
function createLabel() {
	// 生成一个随机数,随机数范围是页面的宽度-label的宽度

	// 1.获取页面的宽度
	var pageWidth = Math.max(document.documentElement.clientWidth,
			document.documentElement.offsetWidth,
			document.documentElement.scrollWidth);
	var labelLeft = getRandom(pageWidth - 10);

	var label = document.createElement("label");
	var colorRandom = "#" + colorchange.charAt(getRandom(16))
			+ colorchange.charAt(getRandom(16))
			+ colorchange.charAt(getRandom(16))
			+ colorchange.charAt(getRandom(16))
			+ colorchange.charAt(getRandom(16))
			+ colorchange.charAt(getRandom(16))
	console.debug(colorRandom);
	label.style.color = colorRandom;
	label.innerHTML = getLabelCode();
	label.style.position = "absolute";
	label.style.top = "0px";
	label.style.left = labelLeft + "px";
	return label;
}

// 页面加载完毕后执行
// window.onload = function(){}

// 用于存储页面中所有label标签
var labelArrays = [];

var num = 0;
var numLabel = null;

function updateNumLabel() {
	numLabel.innerHTML = num;
}

var pageLoad = function() {
	numLabel = document.getElementById("numLabel");
	var tag = true;
	var down ;
	var add;
	var topid;
	var labelid;
	var startGame = document.getElementById("startGame");
	var parse = document.getElementById("parse");
	// startGame.disabled = false;

	startGame.onclick = function() {
		// 点击开始游戏后,禁用按钮
		// startGame.disabled = true;
		startGame.style.display = 'none';
		add=function() {
			var label = createLabel();
			document.body.appendChild(label);
			// 新的label标签加入到labelArrays数组中.
			labelArrays.push(label);
		}
		// 打开一个定时器,用于创建label标签
		labelid = setInterval(add, 1000);

		var pageHeight = Math.max(document.documentElement.clientHeight,
				document.documentElement.offsetHeight,
				document.documentElement.scrollHeight);
		pageHeight = (pageHeight < 300 ? 300 : pageHeight) - 20;

		// 打开一个定时器,用于label标签向下移动
		down = function() {
			for ( var i = 0; i < labelArrays.length; i++) {
				var label = labelArrays[i];
				var top = parseInt(label.style.top);
				top++;
				if (top >= pageHeight) {
					labelArrays.splice(i, 1);
					label.parentNode.removeChild(label);
					num -= 10;
					updateNumLabel();
					continue;
				}
				label.style.top = top + "px";
			}
		}
		topid = setInterval(down, 20);

	};

	parse.onclick = function() {
		if (tag) {
			clearInterval(topid);
			clearInterval(labelid);
			tag = false;
		}
		else{
			labelid = setInterval(add, 1000);
			topid = setInterval(down, 20);
			tag = true;
		}
	}

	document.documentElement.onkeydown = function(event) {
		event = event || window.event;
		var code = String.fromCharCode(event.keyCode);
		for ( var i = 0; i < labelArrays.length; i++) {
			var label = labelArrays[i];
			if (label.innerHTML == code) {
				labelArrays.splice(i, 1);
				label.parentNode.removeChild(label);
				num += 10;
				updateNumLabel();
				break;
			} else if (i == labelArrays.length - 1) {
				num -= 10;
				updateNumLabel();
			}
		}

	};
}
