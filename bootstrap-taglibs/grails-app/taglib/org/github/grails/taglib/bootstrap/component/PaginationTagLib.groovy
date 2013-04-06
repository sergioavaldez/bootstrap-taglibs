package org.github.grails.taglib.bootstrap.component

import static org.github.grails.taglib.bootstrap.Attribute.*;
import org.springframework.web.servlet.support.RequestContextUtils as RCU

class PaginationTagLib {

	static namespace = "bs"
	
	/**
	* Creates next/previous links to support pagination for the current controller.<br/>
	*
	* &lt;g:paginate total="${Account.count()}" /&gt;<br/>
	*
	* @emptyTag
	*
	* @attr total REQUIRED The total number of results to paginate
	* @attr action the name of the action to use in the link, if not specified the default action will be linked
	* @attr controller the name of the controller to use in the link, if not specified the current controller will be linked
	* @attr id The id to use in the link
	* @attr params A map containing request parameters
	* @attr prev The text to display for the previous link (defaults to "Previous" as defined by default.paginate.prev property in I18n messages.properties)
	* @attr next The text to display for the next link (defaults to "Next" as defined by default.paginate.next property in I18n messages.properties)
	* @attr max The number of records displayed per page (defaults to 10). Used ONLY if params.max is empty
	* @attr maxsteps The number of steps displayed for pagination (defaults to 10). Used ONLY if params.maxsteps is empty
	* @attr offset Used only if params.offset is empty
	* @attr fragment The link fragment (often called anchor tag) to use
	* @attr align Align the pagination to left(default), center or right.
	*/
	def paginate = {attrs ->
		def writer = out
		if (attrs.total == null) {
			throwTagError("Tag [paginate] is missing required attribute [total]")
		}

		def messageSource = grailsAttributes.messageSource
		def locale = RCU.getLocale(request)

		def total = attrs.int('total') ?: 0
		def action = (attrs.action ?: (params.action ? params.action : actionName))
		def offset = params.int('offset') ?: 0
		def max = params.int('max')
		def maxsteps = (attrs.int('maxsteps') ?: 10)
		def align = attrs.remove('align')
		
		attrs.remove('total')
		
		if(align == "center" || align == "centered") {
			addToClass(attrs, "pagination-centered")
		} else if(align == "right") {
			addToClass(attrs, "pagination-right")
		}
		
		defaultClasses(attrs, "pagination")
		
		writer << "<div " << outputAttributes(attrs, writer) << ">\n\t<ul>"
		
		if (!offset) offset = (attrs.int('offset') ?: 0)
		if (!max) max = (attrs.int('max') ?: 10)

		def linkParams = [:]
		if (attrs.params) linkParams.putAll(attrs.params)
		linkParams.offset = offset - max
		linkParams.max = max
		if (params.sort) linkParams.sort = params.sort
		if (params.order) linkParams.order = params.order

		def linkTagAttrs = [action: action]
		if (attrs.controller) {
			linkTagAttrs.controller = attrs.controller
		}
		if (attrs.id != null) {
			linkTagAttrs.id = attrs.id
		}
		if (attrs.fragment != null) {
			linkTagAttrs.fragment = attrs.fragment
		}
		linkTagAttrs.params = linkParams

		// determine paging variables
		def steps = maxsteps > 0
		int currentstep = (offset / max) + 1
		int firststep = 1
		int laststep = Math.round(Math.ceil(total / max))

		// display previous link when not on firststep
		if (currentstep > firststep) {
			linkTagAttrs.class = 'prevLink'
			linkParams.offset = offset - max
			writer << "<li>"
			writer << link(linkTagAttrs.clone()) {
				(attrs.prev ?: messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, 'Previous', locale), locale))
			}
			writer << "</li>"
		}

		// display steps when steps are enabled and laststep is not firststep
		if (steps && laststep > firststep) {
			linkTagAttrs.class = 'step'

			// determine begin and endstep paging variables
			int beginstep = currentstep - Math.round(maxsteps / 2) + (maxsteps % 2)
			int endstep = currentstep + Math.round(maxsteps / 2) - 1

			if (beginstep < firststep) {
				beginstep = firststep
				endstep = maxsteps
			}
			if (endstep > laststep) {
				beginstep = laststep - maxsteps + 1
				if (beginstep < firststep) {
					beginstep = firststep
				}
				endstep = laststep
			}

			// display firststep link when beginstep is not firststep
			if (beginstep > firststep) {
				linkParams.offset = 0
				writer << "<li>"
				writer << link(linkTagAttrs.clone()) {firststep.toString()}
				writer << "</li>"
				writer << '<li class="disabled"><a href=\"#\">..</a></li>'
			}

			// display paginate steps
			(beginstep..endstep).each { i ->
				if (currentstep == i) {
					writer << "<li class=\"active\"><a href=\"#\">${i}</a></li>"
				}
				else {
					linkParams.offset = (i - 1) * max
					writer << "<li>" << link(linkTagAttrs.clone()) {i.toString()} << "</li>"
				}
			}

			// display laststep link when endstep is not laststep
			if (endstep < laststep) {
				writer << '<li class="disabled"><a href=\"#\">..</a></li>'
				linkParams.offset = (laststep - 1) * max
				writer << "<li>" << link(linkTagAttrs.clone()) { laststep.toString() } << "</li>"
			}
		}

		// display next link when not on laststep
		if (currentstep < laststep) {
			linkTagAttrs.class = 'nextLink'
			linkParams.offset = offset + max
			writer << "<li>"
			writer << link(linkTagAttrs.clone()) {
				(attrs.next ? attrs.next : messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale))
			}
			writer << "</li>"
		}
		
		writer << "\t</ul>\n</div>"
	}
	
	/**
	* Creates next/previous links to support pagination for the current controller.<br/>
	*
	* &lt;g:paginate total="${Account.count()}" /&gt;<br/>
	*
	* @emptyTag
	*
	* @attr total REQUIRED The total number of results to paginate
	* @attr action the name of the action to use in the link, if not specified the default action will be linked
	* @attr controller the name of the controller to use in the link, if not specified the current controller will be linked
	* @attr id The id to use in the link
	* @attr params A map containing request parameters
	* @attr prev The text to display for the previous link (defaults to "Previous" as defined by default.paginate.prev property in I18n messages.properties)
	* @attr next The text to display for the next link (defaults to "Next" as defined by default.paginate.next property in I18n messages.properties)
	* @attr max The number of records displayed per page (defaults to 10). Used ONLY if params.max is empty
	* @attr maxsteps The number of steps displayed for pagination (defaults to 10). Used ONLY if params.maxsteps is empty
	* @attr offset Used only if params.offset is empty
	* @attr fragment The link fragment (often called anchor tag) to use
	* @attr align Align the pagination to left(default), center or right.
	* @attr useArrows Boolean value that indicates if pager will use arrows.
	*/
	def pager = {attrs ->
		def useArrows = attrs.remove('useArrows')
		def message = null
		def centered = (attrs.remove('centered') == "true")
		def writer = out
		if (attrs.total == null) {
			throwTagError("Tag [paginate] is missing required attribute [total]")
		}

		def messageSource = grailsAttributes.messageSource
		def locale = RCU.getLocale(request)

		def total = attrs.int('total') ?: 0
		def action = (attrs.action ? attrs.action : (params.action ? params.action : "list"))
		def offset = params.int('offset') ?: 0
		def max = params.int('max')
		//def maxsteps = (attrs.int('maxsteps') ?: 10)
		//def align = attrs.remove('align')
		
		attrs.remove('total')
		
		defaultClasses(attrs, "pager")
		
		writer << "<ul " << outputAttributes(attrs, writer) << ">"
		
		if (!offset) offset = (attrs.int('offset') ?: 0)
		if (!max) max = (attrs.int('max') ?: 10)

		def linkParams = [:]
		if (attrs.params) linkParams.putAll(attrs.params)
		linkParams.offset = offset - max
		linkParams.max = max
		if (params.sort) linkParams.sort = params.sort
		if (params.order) linkParams.order = params.order

		def linkTagAttrs = [action: action]
		if (attrs.controller) {
			linkTagAttrs.controller = attrs.controller
		}
		if (attrs.id != null) {
			linkTagAttrs.id = attrs.id
		}
		if (attrs.fragment != null) {
			linkTagAttrs.fragment = attrs.fragment
		}
		linkTagAttrs.params = linkParams

		// determine paging variables
		//def steps = maxsteps > 0
		int currentstep = (offset / max) + 1
		int firststep = 1
		int laststep = Math.round(Math.ceil(total / max))

		// display previous link when not on firststep
		def disabledPrev = null
		if (currentstep == firststep) {
			disabledPrev = "disabled"
		}
		//if (currentstep > firststep) {
			linkTagAttrs.class = 'prevLink'
			linkParams.offset = offset - max
			writer << "<li class=\"${(centered ? '' : 'previous')} ${disabledPrev?:''}\">"
			if(useArrows == "true")
				message = "&larr;&nbsp;"
				
			message += (attrs.prev ?: messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, 'Previous', locale), locale))
			  
			writer << link(linkTagAttrs.clone()) {
				message
			}
			writer << "</li>"
		//}

		// display next link when not on laststep
			def disableNext = null
			if (currentstep >= laststep) {
				disableNext = "disabled";
			}
		//if (currentstep < laststep) {
			linkTagAttrs.class = 'nextLink'
			linkParams.offset = offset + max
			writer << "<li class=\"${(centered ? '' : 'next')} ${disableNext?:''}\">"
			message = (attrs.next ? attrs.next : messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale))
			if(useArrows == "true")
				message += "&nbsp;&rarr;"
				
			writer << link(linkTagAttrs.clone()) {
				message
			}
			writer << "</li>"
		//}
		
		writer << "</ul>"
	}
}
