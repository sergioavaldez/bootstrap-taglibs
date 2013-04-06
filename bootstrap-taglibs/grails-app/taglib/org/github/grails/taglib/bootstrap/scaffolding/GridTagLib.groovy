package org.github.grails.taglib.bootstrap.scaffolding

import org.github.grails.taglib.bootstrap.BaseTagLib;

class GridTagLib extends BaseTagLib{

	static namespace = "bs"
	
	/**
	 * @attrs fluid Boolean attribute that indicates if this row is fluid.
	 * By default <code>false</code> if the parent is a container component and
	 * it is fluid the default value is <code>true</code>.
	 */
	def gridRow = {attrs, body ->
		boolean fluid = Boolean.valueOf(attrs.remove('fluid'))
		String baseClass =  "row${fluid ? '-fluid' : ''}"
		 
		defaultClasses(attrs, baseClass)
		
		out << "<div " << outputAttributes(attrs, out) << ">"
		out << body()
		out << "</div>"
	}
	
	/**
	 * @attr span 1 to 12 by default it is 12.
	 * @attr offset 1 to 12.
	 */
	def gridColumn = {attrs, body ->
		
		if(attrs.offset != null) {
			addToClass(attrs, "offset${attrs.remove('offset')}")
		}
		
		defaultClasses(attrs, "span${(attrs.remove('span') ?: '12')}")
		
		out << "<div " << outputAttributes(attrs, out) << ">"
		out << body()
		out << "</div>"
	}
	
}
