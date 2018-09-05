/*****宽窄屏幕自适应****/
$(document).ready(function(){
if(screen.width > 1420 && $(window).width() > 1420)
{
	$("body").addClass("widemode ");
	$("body").removeClass("middlemode");
}

else{
	$("body").addClass("middlemode ");
	$("body").removeClass("widemode");
	}

});
//当文档窗口发生改变时 触发  
  $(window).resize(function(){
	if(screen.width > 1420 && $(window).width() > 1420)
{
	$("body").addClass("widemode ");
	$("body").removeClass("middlemode");
}
else{
	$("body").addClass("middlemode ");
	$("body").removeClass("widemode ");
	}  
	  
	  
}) ;
$(document).ready(function(){
$(".Video_img_link").hover(function(){
			$(this).addClass("hover");
			//$(this).children(".dorpdown-layer").attr('class','');
		},function(){
			$(this).removeClass("hover");  
			//$(this).children(".dorpdown-layer").attr('class','');
		}
	);
	$(".first_content").hover(function(){
			$(this).addClass("hover");
			//$(this).children(".dorpdown-layer").attr('class','');
		},function(){
			$(this).removeClass("hover");  
			//$(this).children(".dorpdown-layer").attr('class','');
		}
	);
	$(".movie_theme").hover(function(){
			$(this).addClass("hover");
			//$(this).children(".dorpdown-layer").attr('class','');
		},function(){
			$(this).removeClass("hover");  
			//$(this).children(".dorpdown-layer").attr('class','');
		}
	);
		$(".Television_img").hover(function(){
			$(this).addClass("hover");
		},function(){
			$(this).removeClass("hover");  
		}
	);
	$(".Case_info ").hover(function() {
	    $(this).addClass("hover");
		$(this).find(".movie_title").stop()
		.animate({bottom: "0px", opacity:1 , height:"220px"}, "fast")
		.css("display","block")

	}, function() {
	    $(this).removeClass("hover");  
		$(this).find(".movie_title").stop()
		.animate({bottom: "0px", opacity: 1,height:"70px"}, "fast")
		.css("display","block")
	});
/*****************浮动固定********************/
$(window).scroll(function() {
		var topToolbar = $(".home_Column_style");
		var headerH = $("#Preferential_AD").outerHeight();
		var headers = $("#Slideshow,#header_top").outerHeight();
		var scrollTop =$(document.body).scrollTop();	
			if( scrollTop >= headerH + headers ){
				topToolbar.stop(false,true).addClass("fixToTop");
				$(".navigation_list").css("display","none");
				$(".navigation").css("display","block")
			}else if( scrollTop < headerH + headers ){
				topToolbar.stop(false,true).removeClass("fixToTop"); 
				$(".navigation_list").css("display","block");
				$(".navigation").css("display","none")
			}
});
  $(".navigatio_name").hover(function(){
			$(this).addClass("active");
			$(this).children(".navigatio_nav").attr('class','navigatio_nav active');
		},function(){
			$(this).removeClass("active");  
			$(this).children(".navigatio_nav").attr('class','navigatio_nav');
		}
	); 
/**************/
  $(".Channel_link").hover(function(){
			$(this).addClass("active");
			$(this).children(".Channel_nav_list").attr('class','Channel_nav_list active');
		},function(){
			$(this).removeClass("active");  
			$(this).children(".Channel_nav_list").attr('class','Channel_nav_list');
		}
	); 

});
$(function(){
	$(window).on('scroll',function(){
		var st = $(document).scrollTop();
		if( st>0 ){
			if( $('#main-container').length != 0  ){
				var w = $(window).width(),mw = $('#main-container').width();
				if( (w-mw)/2 > 70 )
					$('#go-top').css({'left':(w-mw)/2+mw+20});
				else{
					$('#go-top').css({'left':'auto'});
				}
			}
			$('#go-top').fadeIn(function(){
				$(this).removeClass('dn');
			});
		}else{
			$('#go-top').fadeOut(function(){
				$(this).addClass('dn');
			});
		}	
	});
	$('#go-top .go').on('click',function(){
		$('html,body').animate({'scrollTop':0},500);
	});

	$('#go-top .uc-2vm').hover(function(){
		$('#go-top .uc-2vm-pop').removeClass('dn');
	},function(){
		$('#go-top .uc-2vm-pop').addClass('dn');
	});
});
(function($){
    $.fn.fix = function(options){
        var defaults = {
            float : 'right',
			minStatue : false,
			skin : 'blue',
			durationTime : 1000,
			table_menu:'.videoArea',
			spacingw:18,
        }
        var options = $.extend(defaults, options);		

        this.each(function(){			
            //获取对象
			var thisBox = $(this),
				closeBtn = thisBox.find('.close_btn' ),
				show_btn = thisBox.find('.show_btn' ),
				sideContent = thisBox.find('.listcontrol_content'),
				sideList = thisBox.find('.side_list');	
				stylespacing=thisBox.find(options.table_menu);
			//var defaultTop = thisBox.offset().top;	//对象的默认top	
			
			//thisBox.css(options.float, 0);			
			if(options.minStatue){
				$(".show_btn").css("float", options.float);
				sideContent.css('width', 0);
				show_btn.css('width', 18);
				
			}
			//皮肤控制
			if(options.skin) thisBox.addClass('side_'+options.skin);
				
						
			//核心scroll事件			
//			$(window).bind("scroll",function(){
//				var offsetTop = defaultTop + $(window).scrollTop() + "px";
//	            thisBox.animate({
//	                top: offsetTop
//	            },
//	            {
//	                duration: options.durationTime,	
//	                queue: false    //此动画将不进入动画队列
//	            });
//			});	
			//close事件
			closeBtn.bind("click",function(){
				sideContent.animate({width: '0px'},"fast").css("display","none");
            	show_btn.stop(true, true).delay(300).animate({ width: '18px'},"fast");
				closeBtn.css("display","none");
				show_btn.css("display","block")
				stylespacing.width($(".page_style").width()-(options.spacingw)).addClass("Widescreen");
			});
			//show事件
			 show_btn.click(function() {
	            $(this).animate({width: '0px'},"fast");
	            sideContent.stop(true, true).delay(200).animate({ width: '315px'},"fast").css("display","block");
				closeBtn.css("display","block");
				show_btn.css("display","none");
				stylespacing.width($(".page_style ").width()-315).removeClass("Widescreen");
	        });
				
        });	//end this.each

    };
})(jQuery);