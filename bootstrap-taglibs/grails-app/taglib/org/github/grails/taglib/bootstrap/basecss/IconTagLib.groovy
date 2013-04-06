package org.github.grails.taglib.bootstrap.basecss

import org.github.grails.taglib.bootstrap.BaseTagLib;

class IconTagLib extends BaseTagLib {

	static namespace = "bs"
	
	/**
	 * @attr name REQUIRED Name of icon to use.
	 * @attr white User the white variant of the icon.
	 * @attr size large, 2x, 3x, 4x (Font Awesome feature).
	 * @attr spin Boolean indicates if the icon will be spinning (Font Awesome feature).
	 * @attr bordered Boolean indicates if the icon will be bordered (Font Awesome feature).
	 * @attr muted Boolean indicates if the icon will be muted (Font Awesome feature).
	 * */
	def icon = {attrs ->
		def iconName = attrs.remove('name')?:attrs.remove('icon')
		 
		if(attrs.size) {
			addToClass(attrs, "icon-${attrs.remove('size')}")
		}
		
		if(Boolean.valueOf(attrs.remove('spin'))) {
			addToClass(attrs, "icon-spin")
		}
		
		if(Boolean.valueOf(attrs.remove('bordered'))) {
			addToClass(attrs, "icon-border")
		}
		
		if(Boolean.valueOf(attrs.remove('muted'))) {
			addToClass(attrs, "icon-muted")
		}
		
		if(Boolean.valueOf(attrs.remove('white'))) {
			addToClass(attrs, "icon-white")
		}
		
		defaultClasses(attrs, "icon-$iconName")
		
		out << "<i " << outputAttributes(attrs, out) << "></i>"
	}
}
