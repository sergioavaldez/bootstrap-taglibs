package org.github.grails.taglib.bootstrap.scaffolding

import org.github.grails.taglib.bootstrap.BaseTagLib;

class LayoutTagLib extends BaseTagLib{

	static namespace = "bs"
	
	/**
	 * 
	 * 
	 * @attr fluid The fluid grid system uses percents instead of pixels for column widths.
	 * @attr class All classes specified in this property will be added to the base class.
	 */
	def container = {attrs, body ->
		boolean fluid = Boolean.valueOf(attrs.remove('fluid'))
		String baseClass = "container${fluid ? '-fluid' : ''}"
		
		defaultClasses(attrs, baseClass)
		 	
		out << "<div " << outputAttributes(attrs, out) << ">"
		out << body()
		out << "</div>"
	}
}
