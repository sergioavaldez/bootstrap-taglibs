package org.github.grails.taglib.bootstrap.component

import org.github.grails.taglib.bootstrap.BaseTagLib;

class AlertTagLib extends BaseTagLib {

	static namespace = "bs"
	
	/**
	 * attrs decorate Some of these values: <code>info</code>, <code>success</code>, <code>warning</code> or <code>error</code>,
	 */
	def alert = {attrs, body ->
		def closable = attrs.remove('closable') 
		
		if(attrs.'decorate') {
			addToClass(attrs, "alert-${attrs.remove('decorate')}")
		}
		
		if(attrs.remove('block') == "true") {
			addToClass(attrs, "alert-block")
		}
		
		defaultClasses(attrs, "alert")
		
		out << "<div " << outputAttributes(attrs, out) << ">"
		if(closable == 'true') {
			out << '\t<button type="button" class="close" data-dismiss="alert">&times;</button>'
		}
		out << body()
		out << "</div>"
	}
	
}
