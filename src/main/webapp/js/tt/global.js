var Obj;
if (!Obj) {
	Obj = {};
}
/**
 * favs 加入收藏试题id集合
 * @type 
 */
Obj.favs = [];
Array.prototype.indexof = function(item) {
	for (var i=0; i<this.length; i++) {
		var bool = true;
		for (var x in item){
			bool = (item[x] == this[i][x]) && bool;
		}
		if (bool) return i;
	}
	return -1;
};
Array.prototype.removeObj = function(b) { 
	var a = this.indexOf(b); 
	if (a >= 0) { 
	/**
	 * splice(添加或删除项目的位置,添加或修改项目的数量,添加的新项目1...n)
	 */
	this.splice(a, 1); 
		return true; 
	} 
	return false; 
};
(function() {
	/**
	 * 添加收藏
	 */
	Obj.switchfavorite = function(id,_this) {
		var $parent = $(_this);
		var $target = $parent.children()[0];
		if ($($target).attr('class') == 'octicon octicon-plus') {
			$($target).attr('class', 'octicon octicon-x');
			$parent.attr('data-original-title','移除收藏');
			Obj.favs.push(id);
		} else if ($($target).attr('class') == 'octicon octicon-x') {
			$($target).attr('class', 'octicon octicon-plus');
			$parent.attr('data-original-title','加入收藏');
			Obj.favs.removeObj(id);
		}
	};
})();
