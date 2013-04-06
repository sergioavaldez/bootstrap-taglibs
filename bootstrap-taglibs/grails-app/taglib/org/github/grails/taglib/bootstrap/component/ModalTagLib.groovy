package org.github.grails.taglib.bootstrap.component

import org.github.grails.taglib.bootstrap.BaseTagLib;

class ModalTagLib extends BaseTagLib {

	static namespace = "bs"
	
	private static final String CLOSABLE 	= "bsTLModalClosable"
	private static final String ELEMENT_ID 	= "bsTLModalID"
	
	/**
	 * Modal are streamlined, but flexible, dialog prompts with the minimum 
	 * required functionality and smart defaults.
	 * 
	 * @attr backdrop Boolean attribute that indicated to include a modal-backdrop
	 * element. Alternatively, specify <code>static</code> for a backdrop which 
	 * doesn't close the modal on click. <code>true</code> by default.
	 * 
	 * @attrs keyboard Boolean attribute that indicated to close the modal when 
	 * escape key is pressed. <code>true</code> by default.
	 * 
	 * @attrs show Boolean attribute that indicated to show the modal when 
	 * initialized. <code>true</code> by default.
	 * 
	 * @attrs remote If a remote url is provided, content will be loaded via 
	 * jQuery's load method and injected into the .modal-body. If you're using 
	 * the data api, you may alternatively use the <code>href</code> tag to 
	 * specify the remote source. An example of this is shown below:
	 * <code>
	 * <a data-toggle="modal" href="remote.html" data-target="#modal">click me</a>
	 * </code>
	 * 
	 * @attrs closable Boolean value. <code>true</code> by default.
	 * 
	 * @attrs hide Boolean value. <code>false</code> by default.
	 * 
	 * @attrs fade Boolean value. <code>false</code> by default.
	 */
	def modal = {attrs, body->
		createId(attrs)
		currentComponentType("modal")
		
		boolean closable 	= true
				
		attrs.tabindex 	= "-1"
		attrs.role 		= "dialog"
		
		if(attrs.closable != null) {
			closable = (attrs.remove('closable') == 'true')
		} 
		
		if(attrs.show == 'false') {
			addToClass(attrs, "hide fade")
		}
		
		addToDataAPI(attrs, "backdrop")
		addToDataAPI(attrs, "keyboard")
		addToDataAPI(attrs, "show")
		addToDataAPI(attrs, "remote")
		
		pageScope."$CLOSABLE" = closable
		
		if(Boolean.valueOf(attrs.remove('hide'))) {
			addToClass(attrs, "hide")
		}
		
		if(Boolean.valueOf(attrs.remove('fade'))) {
			addToClass(attrs, "fade")
		}
		
		defaultClasses(attrs, "modal")
		out << "<div " << outputAttributes(attrs, out) << ">"
		out << body(attrs.id)
		out << "</div>"
		
		pageScope."$CLOSABLE" 	= null
		cleanCurrentComponent()
	}
	
	def modalHeader = {attrs, body->
		defaultClasses(attrs, "modal-header")
		out << "<div " << outputAttributes(attrs, out) << ">"
		if(pageScope."$CLOSABLE") {
			out << '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'
		}
		out << body()
		out << "</div>"
	}
	
	def modalBody = {attrs, body->
		defaultClasses(attrs, "modal-body")
		out << "<div " << outputAttributes(attrs, out) << ">"
		out << body()
		out << "</div>"
	}
	
	def modalFooter = {attrs, body->
		defaultClasses(attrs, "modal-footer")
		out << "<div " << outputAttributes(attrs, out) << ">"
		out << body()
		out << "</div>"
	}
}
