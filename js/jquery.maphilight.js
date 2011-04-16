(function($) {
	var has_VML, create_canvas_for, add_shape_to, clear_canvas, shape_from_area,
		canvas_style, fader, hex_to_decimal, css3color, is_image_loaded, options_from_area;

	has_VML = document.namespaces;
	has_canvas = !!document.createElement('canvas').getContext;

	if(!(has_canvas || has_VML)) {
		$.fn.maphilight = function() { return this; };
		return;
	}
	
	if(has_canvas) {
		fader = function(element, opacity, interval) {
			if(opacity <= 1) {
				element.style.opacity = opacity;
				window.setTimeout(fader, 10, element, opacity + 0.1, 10);
			}
		};
		
		hex_to_decimal = function(hex) {
			return Math.max(0, Math.min(parseInt(hex, 16), 255));
		};
		css3color = function(color, opacity) {
			return 'rgba('+hex_to_decimal(color.substr(0,2))+','+hex_to_decimal(color.substr(2,2))+','+hex_to_decimal(color.substr(4,2))+','+opacity+')';
		};
		create_canvas_for = function(img) {
			var c = $('<canvas style="width:'+img.width+'px;height:'+img.height+'px;"></canvas>').get(0);
			c.getContext("2d").clearRect(0, 0, c.width, c.height);
			return c;
		};
		add_shape_to = function(canvas, shape, coords, options, name) {
			//console.log('adding shape');
			var i, context = canvas.getContext('2d');
			context.beginPath();
			if(shape == 'rect') {
				context.rect(coords[0], coords[1], coords[2] - coords[0], coords[3] - coords[1]);
			} else if(shape == 'poly') {
				context.moveTo(coords[0], coords[1]);
				for(i=2; i < coords.length; i+=2) {
					context.lineTo(coords[i], coords[i+1]);
				}
			} else if(shape == 'circ') {
				context.arc(coords[0], coords[1], coords[2], 0, Math.PI * 2, false);
			}
			context.closePath();
			if(options.fill) {
				context.fillStyle = css3color(options.fillColor, options.fillOpacity);
				context.fill();
			}
			if(options.stroke) {
				context.strokeStyle = css3color(options.strokeColor, options.strokeOpacity);
				context.lineWidth = options.strokeWidth;
				context.stroke();
			}
			if(options.fade) {
				fader(canvas, 0);
			}
		};
		clear_canvas = function(canvas, area) {
			canvas.getContext('2d').clearRect(0, 0, canvas.width,canvas.height);
		};
	} else {   // ie executes this code
		create_canvas_for = function(img) {
			return $('<var style="zoom:1;overflow:hidden;display:block;width:'+img.width+'px;height:'+img.height+'px;"></var>').get(0);
		};
		add_shape_to = function(canvas, shape, coords, options, name) {
			//console.log('adding shape ie');
			var fill, stroke, opacity, e;
			fill = '<v:fill color="#'+options.fillColor+'" opacity="'+(options.fill ? options.fillOpacity : 0)+'" />';
			stroke = (options.stroke ? 'strokeweight="'+options.strokeWidth+'" stroked="t" strokecolor="#'+options.strokeColor+'"' : 'stroked="f"');
			opacity = '<v:stroke opacity="'+options.strokeOpacity+'"/>';
			if(shape == 'rect') {
				e = $('<v:rect name="'+name+'" filled="t" '+stroke+' style="zoom:1;margin:0;padding:0;display:block;position:absolute;left:'+coords[0]+'px;top:'+coords[1]+'px;width:'+(coords[2] - coords[0])+'px;height:'+(coords[3] - coords[1])+'px;"></v:rect>');
			} else if(shape == 'poly') {
				e = $('<v:shape name="'+name+'" filled="t" '+stroke+' coordorigin="0,0" coordsize="'+canvas.width+','+canvas.height+'" path="m '+coords[0]+','+coords[1]+' l '+coords.join(',')+' x e" style="zoom:1;margin:0;padding:0;display:block;position:absolute;top:0px;left:0px;width:'+canvas.width+'px;height:'+canvas.height+'px;"></v:shape>');
			} else if(shape == 'circ') {
				e = $('<v:oval name="'+name+'" filled="t" '+stroke+' style="zoom:1;margin:0;padding:0;display:block;position:absolute;left:'+(coords[0] - coords[2])+'px;top:'+(coords[1] - coords[2])+'px;width:'+(coords[2]*2)+'px;height:'+(coords[2]*2)+'px;"></v:oval>');
			}
			e.get(0).innerHTML = fill+opacity;
			$(canvas).append(e);
		};
		clear_canvas = function(canvas) {
			$(canvas).find('[name=highlighted]').remove();
		};
	}
	
	shape_from_area = function(area) {
		var i, coords = area.getAttribute('coords').split(',');
		for (i=0; i < coords.length; i++) { coords[i] = parseFloat(coords[i]); }
		return [area.getAttribute('shape').toLowerCase().substr(0,4), coords];
	};

	options_from_area = function(area, options) {
		var $area = $(area);
		return $.extend({}, options, $.metadata ? $area.metadata() : false, $area.data('maphilight'));
	};
	
	is_image_loaded = function(img) {
		if(!img.complete) { return false; } // IE
		if(typeof img.naturalWidth != "undefined" && img.naturalWidth == 0) { return false; } // Others
		return true;
	};

	canvas_style = {
		position: 'absolute',
		left: 0,
		top: 0,
		padding: 0,
		border: 0
	};
	
	var ie_hax_done = false;
	$.fn.maphilight = function(opts) {
		opts = $.extend({}, $.fn.maphilight.defaults, opts);
		
		if($.browser.msie && !ie_hax_done) {
			document.namespaces.add("v", "urn:schemas-microsoft-com:vml");
			var style = document.createStyleSheet();
			var shapes = ['shape','rect', 'oval', 'circ', 'fill', 'stroke', 'imagedata', 'group','textbox'];
			$.each(shapes,
				function() {
					style.addRule('v\\:' + this, "behavior: url(#default#VML); antialias:true");
				}
			);
			ie_hax_done = true;
		}
		
		return this.each(function() {
			var img, wrap, options, map, canvas, canvas_always, mouseover, highlighted_shape, usemap;
			img = $(this);

			if(!is_image_loaded(this)) {
				// If the image isn't fully loaded, this won't work right.  Try again later.
				return window.setTimeout(function() {
					img.maphilight(opts);
				}, 200);
			}

			options = $.extend({}, opts, $.metadata ? img.metadata() : false, img.data('maphilight'));

			// jQuery bug with Opera, results in full-url#usemap being returned from jQuery's attr.
			// So use raw getAttribute instead.
			usemap = img.get(0).getAttribute('usemap');

			map = $('map[name="'+usemap.substr(1)+'"]');

			if(!(img.is('img') && usemap && map.size() > 0)) { return; }

			if(img.hasClass('maphilighted')) {
				// We're redrawing an old map, probably to pick up changes to the options.
				// Just clear out all the old stuff.
				var wrapper = img.parent();
				img.insertBefore(wrapper);
				wrapper.remove();
			}

			wrap = $('<div></div>').css({
				display:'block',
				background:'url('+this.src+')',
				position:'relative',
				padding:0,
				width:this.width,
				height:this.height
				});
			if(options.wrapClass) {
				if(options.wrapClass === true) {
					wrap.addClass($(this).attr('class'));
				} else {
					wrap.addClass(options.wrapClass);
				}
			}
			img.before(wrap).css('opacity', 0).css(canvas_style).remove();
			if($.browser.msie) { img.css('filter', 'Alpha(opacity=0)'); }
			wrap.append(img);
			
			canvas = create_canvas_for(this);
			$(canvas).css(canvas_style);
			canvas.height = this.height;
			canvas.width = this.width;
			
			mouseover = function(e) {
				var shape, area_options;
				area_options = options_from_area(this, options);
				/*console.log(area_options);
				if(
					!area_options.neverOn
					&&
					!area_options.alwaysOn
				) {*/
					shape = shape_from_area(this);
					add_shape_to(canvas, shape[0], shape[1], area_options, "highlighted");
					/*if(area_options.groupBy && $(this).attr(area_options.groupBy)) {
						var first = this;
						map.find('area['+area_options.groupBy+'='+$(this).attr(area_options.groupBy)+']').each(function() {
							if(this != first) {
								var subarea_options = options_from_area(this, options);
								if(!subarea_options.neverOn && !subarea_options.alwaysOn) {
									var shape = shape_from_area(this);
									add_shape_to(canvas, shape[0], shape[1], subarea_options, "highlighted");
								}
							}
						});
					}
				}*/
			}
			
			if(options.alwaysOn) {
				//console.log('always on');
				$(map).find('area[coords]').each(mouseover);
			} else {
				// If the metadata plugin is present, there may be areas with alwaysOn set.
				// We'll add these to a *second* canvas, which will get around flickering during fading.
				$(map).find('area[coords]').each(function() {
					var shape, area_options;
					area_options = options_from_area(this, options);
					if(area_options.alwaysOn) {
						if(!canvas_always) {
							canvas_always = create_canvas_for(img.get());
							$(canvas_always).css(canvas_style);
							canvas_always.width = img.width();
							canvas_always.height = img.height();
							img.before(canvas_always);
						}
						shape = shape_from_area(this);
						if ($.browser.msie) {
							add_shape_to(canvas, shape[0], shape[1], area_options, "");
						} else {
							add_shape_to(canvas_always, shape[0], shape[1], area_options, "");
						}
					}
				});
				$(map).find('area[coords]').mouseover(mouseover).mouseout(function(e) { clear_canvas(canvas); });
			}
			
			img.before(canvas); // if we put this after, the mouseover events wouldn't fire.
			
			img.addClass('maphilighted');
		});
	};
	$.fn.maphilight.defaults = {
			fill: true,
			fillColor: 'ffffff',
			fillOpacity: 0.3,
			stroke: true,
			strokeColor: 'ff0000',
			strokeOpacity: 1,
			strokeWidth: 1,
			fade: false,
			alwaysOn: true,
			neverOn: false,
			groupBy: false,
			wrapClass: false
		};
})(jQuery);

/*(function($) {
	var has_VML, create_canvas_for, add_shape_to, clear_canvas, shape_from_area,
		canvas_style, fader, hex_to_decimal, css3color, is_image_loaded;
	has_VML = document.namespaces;
	has_canvas = document.createElement('canvas');
	has_canvas = has_canvas && has_canvas.getContext;

	if(!(has_canvas || has_VML)) {
		$.fn.maphilight = function() { return this; };
		return;
	}
	
	if(has_canvas) {
		fader = function(element, opacity, interval) {
			if(opacity <= 1) {
				element.style.opacity = opacity;
				window.setTimeout(fader, 10, element, opacity + 0.1, 10);
			}
		};
		
		hex_to_decimal = function(hex) {
			return Math.max(0, Math.min(parseInt(hex, 16), 255));
		};
		css3color = function(color, opacity) {
			return 'rgba('+hex_to_decimal(color.substr(0,2))+','+hex_to_decimal(color.substr(2,2))+','+hex_to_decimal(color.substr(4,2))+','+opacity+')';
		};
		create_canvas_for = function(img) {
			var c = $('<canvas style="width:'+img.width+'px;height:'+img.height+'px;"></canvas>').get(0);
			c.getContext("2d").clearRect(0, 0, c.width, c.height);
			return c;
		};
		add_shape_to = function(canvas, shape, coords, options) {
			//console.log('adding shape');
			var i, context = canvas.getContext('2d');
			context.beginPath();
			if(shape == 'rect') {
				context.rect(coords[0], coords[1], coords[2] - coords[0], coords[3] - coords[1]);
			} else if(shape == 'poly') {
				context.moveTo(coords[0], coords[1]);
				for(i=2; i < coords.length; i+=2) {
					context.lineTo(coords[i], coords[i+1]);
				}
			} else if(shape == 'circ') {
				context.arc(coords[0], coords[1], coords[2], 0, Math.PI * 2, false);
			}
			context.closePath();
			if(options.fill) {
				context.fillStyle = css3color(options.fillColor, options.fillOpacity);
				context.fill();
			}
			if(options.stroke) {
				context.strokeStyle = css3color(options.strokeColor, options.strokeOpacity);
				context.lineWidth = options.strokeWidth;
				context.stroke();
			}
			if(options.fade) {
				fader(canvas, 0);
			}
		};
		clear_canvas = function(canvas, area) {
			canvas.getContext('2d').clearRect(0, 0, canvas.width,canvas.height);
		};
	} else {
		//document.createStyleSheet().addRule("v\\:*", "behavior: url(#default#VML); antialias: true;"); 
		//document.namespaces.add("v", "urn:schemas-microsoft-com:vml"); 
		document.namespaces.add("v", "urn:schemas-microsoft-com:vml");  
		var style = document.createStyleSheet(); 
		var shapes = ['shape','rect', 'oval', 'circ', 'fill', 'stroke', 'imagedata', 'group','textbox'];   
		$.each(shapes, 
			function() 
			{ 
				style.addRule('v\\:' + this, "behavior: url(#default#VML); antialias:true"); 
			} 
		); 

		create_canvas_for = function(img) {
			return $('<var style="zoom:1;overflow:hidden;display:block;width:'+img.width+'px;height:'+img.height+'px;"></var>').get(0);
		};
		add_shape_to = function(canvas, shape, coords, options) {
			var fill, stroke, opacity, e;
			fill = '<v:fill color="#'+options.fillColor+'" opacity="'+(options.fill ? options.fillOpacity : 0)+'" />';
			stroke = (options.stroke ? 'strokeweight="'+options.strokeWidth+'" stroked="t" strokecolor="#'+options.strokeColor+'"' : 'stroked="f"');
			opacity = '<v:stroke opacity="'+options.strokeOpacity+'"/>';
			if(shape == 'rect') {
				e = $('<v:rect filled="t" '+stroke+' style="zoom:1;margin:0;padding:0;display:block;position:absolute;left:'+coords[0]+'px;top:'+coords[1]+'px;width:'+(coords[2] - coords[0])+'px;height:'+(coords[3] - coords[1])+'px;"></v:rect>');
			} else if(shape == 'poly') {
				e = $('<v:shape filled="t" '+stroke+' coordorigin="0,0" coordsize="'+canvas.width+','+canvas.height+'" path="m '+coords[0]+','+coords[1]+' l '+coords.join(',')+' x e" style="zoom:1;margin:0;padding:0;display:block;position:absolute;top:0px;left:0px;width:'+canvas.width+'px;height:'+canvas.height+'px;"></v:shape>');
			} else if(shape == 'circ') {
				e = $('<v:oval filled="t" '+stroke+' style="zoom:1;margin:0;padding:0;display:block;position:absolute;left:'+(coords[0] - coords[2])+'px;top:'+(coords[1] - coords[2])+'px;width:'+(coords[2]*2)+'px;height:'+(coords[2]*2)+'px;"></v:oval>');
			}
			e.get(0).innerHTML = fill+opacity;
			$(canvas).append(e);
		};
		clear_canvas = function(canvas) {
			$(canvas).empty();
		};
	}
	shape_from_area = function(area) {
		var i, coords = area.getAttribute('coords').split(',');
		for (i=0; i < coords.length; i++) { coords[i] = parseFloat(coords[i]); }
		return [area.getAttribute('shape').toLowerCase().substr(0,4), coords];
	};
	
	is_image_loaded = function(img) {
		if(!img.complete) { return false; } // IE
		if(typeof img.naturalWidth != "undefined" && img.naturalWidth == 0) { return false; } // Others
		return true;
	}

	canvas_style = {
		position: 'absolute',
		left: 0,
		top: 0,
		padding: 0,
		border: 0
	};
	
	$.fn.maphilight = function(opts) {
	
		opts = $.extend({}, $.fn.maphilight.defaults, opts);
		return this.each(function() {
			//console.log('mapping');
			var img, wrap, options, map, canvas, mouseover, mapName; //mapName added
			img = $(this);
			if(!is_image_loaded(this)) { return window.setTimeout(function() { img.maphilight(); }, 200); }
			options = $.metadata ? $.extend({}, opts, img.metadata()) : opts;
			//added to make it work in Opera which has an abnormal return value for img.attr('usemap')
			//var pathName = document.location.pathname;
			//pathName.replace(" ", "%20");
			//console.log('doc', pathName);
			//console.log('locations:', img.attr('usemap').indexOf(pathName), img.attr('usemap'));
			if( (img.attr('usemap').indexOf('.htm') !== -1) || //a few doctypes and domain names that indicate that the full URL has been returned
					(img.attr('usemap').indexOf('.php') !== -1) ||
					(img.attr('usemap').indexOf('.asp') !== -1) ||
					(img.attr('usemap').indexOf('.net') !== -1) ||
					(img.attr('usemap').indexOf('.com') !== -1) ||
					(img.attr('usemap').indexOf('.co.') !== -1) ||
					(img.attr('usemap').indexOf('.org') !== -1) /*$('.opera').length > 0*//*){ 
				//console.log('opera');
				var domainLength = document.location.href.length;
				if( document.location.href.indexOf('#') === -1){
					domainLength++;
				}
				if(document.location.hash.length > 0){
					domainLength -= document.location.hash.length;
					domainLength++;
				}
				mapName = img.attr('usemap').substr(domainLength);
			} else {
				//console.log('not opera');
				mapName = img.attr('usemap').substr(1);
			}
			map = $('map[name="'+mapName+'"]');
			//console.log('before out', img, img.is('img'), img.attr('usemap'), map.size(), img.attr('usemap').substr(1),
			//	document.location.href, mapName, document.location.href.length, document.location.hash);
			if(!(img.is('img') && img.attr('usemap') && map.size() > 0)) { return; }
			//console.log('may have returned');
			if(img.hasClass('maphilighted')) {
				// We're redrawing an old map, probably to pick up changes to the options.
				// Just clear out all the old stuff.
				var wrapper = img.parent();
				img.insertBefore(wrapper);
				wrapper.remove();
			}
			wrap = $('<div>').css({display:'block',background:'url('+this.src+')',position:'relative',padding:0,width:this.width,height:this.height});
			img.before(wrap).css('opacity', 0).css(canvas_style).remove();
			if($.browser.msie) { img.css('filter', 'Alpha(opacity=0)'); }
			wrap.append(img);
			
			canvas = create_canvas_for(this);
			$(canvas).css(canvas_style);
			canvas.height = this.height;
			canvas.width = this.width;
			
			mouseover = function(e) {
				//console.log('doing', e);
				var shape = shape_from_area(this);
				add_shape_to(canvas, shape[0], shape[1], $.metadata ? $.extend({}, options, $(this).metadata()) : options);
			};
			//console.log(options);
			if(options.alwaysOn) {
				$(map).find('area[coords]').each(mouseover);
			} else {
				$(map).find('area[coords]').mouseover(mouseover).mouseout(function(e) { clear_canvas(canvas); });
			}
			
			img.before(canvas); // if we put this after, the mouseover events wouldn't fire.
			img.addClass('maphilighted');
			//window.open(canvas.toDataURL());
		});
	};
	$.fn.maphilight.defaults = {
		fill: true,
		fillColor: '000000',
		fillOpacity: 0.2,
		stroke: true,
		strokeColor: 'ff0000',
		strokeOpacity: 1,
		strokeWidth: 1,
		fade: true,
		alwaysOn: false
	};
})(jQuery);*/