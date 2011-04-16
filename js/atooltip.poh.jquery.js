/*
	jQuery Version:				jQuery 1.3.2
	Plugin Name:				aToolTip V 1.0
	Plugin by: 					Ara Abcarians: http://ara-abcarians.com
	License:					aToolTip is licensed under a Creative Commons Attribution 3.0 Unported License
								Read more about this license at --> http://creativecommons.org/licenses/by/3.0/	
	Note:		Heavily modified to work as wanted with the POHPlanner
*/
(function($) {
    $.fn.aToolTip = function(options) {
    
    	// setup default settings
    	var defaults = {
    		fixed: false,
    		outSpeed: 0,
    		toolTipClass: 'aToolTip',
    		xOffset: 5,
    		yOffset: 5
    	},
    
    	// This makes it so the users custom options overrides the default ones
    	settings = $.extend({}, defaults, options);
    
		return this.each(function() {
			var obj = $(this);
			var tipContent;
			obj.attr({title: ''});	

			// Activate on hover	
			obj.unbind('hover');
			
			obj.hover(function(el){
				//console.log(obj.data('tooltipText'));
				//for(var q = 0; q < 10; q++){
				tipContent = obj.data('tooltipText');
				if(typeof tipContent === 'undefined'){
					return;
				}				  
				$('body').append("<div class='"+ settings.toolTipClass +"'><p class='aToolTipContent'>"+ tipContent +"</p></div>");
				$('.' + settings.toolTipClass).css({
					position: 'absolute',
					display: 'none',
					zIndex: '50000',
					top: (obj.offset().top - $('.' + settings.toolTipClass).outerHeight() - settings.yOffset) + 'px',
					left: (obj.offset().left + obj.outerWidth() + settings.xOffset) + 'px'
				})
				.stop()
				.show();
				//.fadeIn(settings.inSpeed);	
				//}
			},
			function(){ 
				// Fade out
				$('.' + settings.toolTipClass).stop().fadeOut(settings.outSpeed, function(){$(this).remove();});
			});	

		    // Follow mouse if fixed is false and click is false
			obj.mousemove(function(el){
				$('.' + settings.toolTipClass).css({
					top: (el.pageY - $('.' + settings.toolTipClass).outerHeight() - settings.yOffset),
					left: (el.pageX + settings.xOffset)
				});
				//console.log($('.' + settings.toolTipClass).width());
				//console.log('p:', $('.' + settings.toolTipClass + ' p').width());
				//console.log('x', el.pageX, $(document).width(),
				//	$('.' + settings.toolTipClass).width() + el.pageX + settings.xOffset);
				//$('.' + settings.toolTipClass + ' p').width($('.' + settings.toolTipClass).width());
			});			
		  
		}); // END: return this
		
		// returns the jQuery object to allow for chainability.  
        return this;
    };
})(jQuery);