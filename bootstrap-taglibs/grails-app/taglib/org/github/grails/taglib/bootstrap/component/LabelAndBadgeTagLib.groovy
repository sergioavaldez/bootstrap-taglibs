package org.github.grails.taglib.bootstrap.component

import static org.github.grails.taglib.bootstrap.Attribute.*;

class LabelAndBadgeTagLib {

	static namespace = "bs"
	
	/**
	 * @attr decorate Can be success, warning, important, info or inverse. 
	 * 
	 */
	def label = {attrs, body ->
		attrs.component = 'label'
		
		spanImpl(attrs, body, out)
	}
	
	/**
	* @attr decorate Can be success, warning, important, info or inverse.
	*/
	def badge = {attrs, body ->
		attrs.component = 'badge'
		
		spanImpl(attrs, body, out)
	}
	
	void spanImpl(attrs, body, out) {
		
		if(attrs.'decorate') {
			addToClass(attrs, "${attrs.'component'}-${attrs.remove('decorate')}")
		}
		
		defaultClasses(attrs, "${attrs.remove('component')}")
		
		out << "<span " << outputAttributes(attrs, out) << ">"
		out << body()
		out << "</span>"
	} 
	
}
