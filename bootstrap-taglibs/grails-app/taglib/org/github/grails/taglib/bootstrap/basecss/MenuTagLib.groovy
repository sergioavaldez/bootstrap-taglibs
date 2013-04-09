package org.github.grails.taglib.bootstrap.basecss

import static org.github.grails.taglib.bootstrap.Attribute.*;

class MenuTagLib {

	static namespace = "bs"
	
	private static final String IN_DROPDOWN_MENU 		  	= "inDropdownMenu"
	private static final String IN_DROPDOWN_ITEM 		  	= "inDropdownItem"
	private static final String IN_DROPDOWN_SUBMENU_ITEM	= "inDropdownSubmenuItem"
	private static final String DROPDOWN_SUBMENU_TOGGLE		= "inDropdownSubmenuToggle"
	private static final String DROPDOWN_MENU_RENDERED_AS 	= "dropdownMenuRenderedAs"
	
	def navigation = {attrs, body ->
		defaultClasses(attrs, "nav")
		
		out << "<ul " << outputAttributes(attrs, out) << ">"
		out << body()
		out << "</ul>"
	}
	
	def navigationItem = {attrs, body ->
		defaultClasses(attrs, "${attrs.remove('class') ?: ''} ${attrs.'isDropdown' == 'true' ? ' dropdown' : ''}")
		def useLink = (!attrs.useLink || attrs.useLink == 'true') ? true : false
		
		
		if(Boolean.valueOf(attrs.remove('dropdown'))) {
			out << submenuItem(attrs){body()}
		} else {
			attrs.remove('useLink')
			
			out << "<li " << outputAttributes(attrs, out)  << ">"
			if(useLink) {
				out << g.link(attrs){body()}
			} else {
				out << body()
			}
			
			out << "</li>"
		}
	}
	
	/**
	 * 
	 * @attrs renderAs Render this element as any element, by default <code>link</code>.
	 * @attrs useCaret Boolean value, indicates when use caret, by default <code>false</code>.
	 */
	def dropdownToggle = {attrs, body ->
		def renderAs = attrs.remove('renderAs') ?: 'link' 
		def useCaret = attrs.remove('useCaret') as boolean
		
		attrs."data-toggle" = "dropdown"
		
		defaultClasses(attrs, "dropdown-toggle")
		
		if(renderAs == 'link') {
			out << "<a href=\"#\" " << outputAttributes(attrs, out) << ">"
			out << body() + (useCaret ? "<span class=\"caret\"></span>" : '')
			out << "</a>"
		} 
		
		
	}
	
	def dropdownMenu = {attrs, body ->
		pageScope."$IN_DROPDOWN_MENU" = true
		
		def renderAs = attrs.remove('renderAs') ?: 'ul'
		
		pageScope."$DROPDOWN_MENU_RENDERED_AS" = renderAs
		
		defaultClasses(attrs, "dropdown-menu")
		
		attrs.role = "menu"
		attrs."aria-labelledby" = "dropdownMenu" 
		
		out << "<$renderAs " << outputAttributes(attrs, out) << ">"
		out << body()
		out << "</$renderAs>"
		
		pageScope."$IN_DROPDOWN_MENU" 			= null
		pageScope."$DROPDOWN_MENU_RENDERED_AS" 	= null
	}
	
	def submenuItem = {attrs, body ->
		attrs.isSubmenu = "true"
		out << menuItem(attrs){body()}
	}
	
	
	/**
	 * @attrs selected Boolean value.
	 */
	def menuItemSelectable = {attrs, body->
		boolean selected = attrs.boolean('selected')
		
		attrs.remove('selected')
		out << menuItem(attrs){ 
				"${selected ? bs.icon(icon:'check') : bs.icon(icon:'check-empty')} ${body()}"
			}	
	}
	
	/**
	 * @attr isSubmenu Indicate if this is a submenu.
	 * @attr useLink Indicates if use link </code>true<code> by default. 
	 */
	def menuItem = {attrs, body ->
		pageScope."$IN_DROPDOWN_ITEM" = true
		
		def renderAs = (!attrs.renderAs || pageScope."$DROPDOWN_MENU_RENDERED_AS" == 'ul') ? 'li' : attrs.remove('renderAs')
		def baseClass = "${attrs.remove('class') ?: ''} ${attrs.isSubmenu == 'true' ? ' dropdown-submenu' : ''}"
		def useLink = (!attrs.useLink || attrs.useLink == 'true') ? true : false
		
		attrs.remove('useLink')
		
		if(useLink) {
			attrs.tabindex = "-1"
		}
		
		out << "<$renderAs " << outputAttributes(baseClass ? [class: baseClass] : [], out)  << ">"
		if(useLink) {
			if(attrs.remove('isSubmenu') == 'true') {
				out << body()
			} else {
				out << g.link(attrs){body()}
			}
		} else {
			out << body()
		}
		
		out << "</$renderAs>"
		
		pageScope."$IN_DROPDOWN_ITEM" = null
	}
	
	def menuDivider = {attrs ->
		def renderAs = (!attrs.renderAs || pageScope."$DROPDOWN_MENU_RENDERED_AS" == 'ul') ? 'li' : attrs.remove('renderAs')
		
		defaultClasses(attrs, "divider")
		
		out << "<$renderAs " << outputAttributes(attrs, out) << "></$renderAs>"
	}
	
}
