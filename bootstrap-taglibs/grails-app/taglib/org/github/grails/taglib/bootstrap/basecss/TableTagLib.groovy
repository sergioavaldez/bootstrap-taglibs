package org.github.grails.taglib.bootstrap.basecss

import org.codehaus.groovy.grails.web.servlet.FlashScope;
import org.github.grails.taglib.bootstrap.BaseTagLib;
import org.springframework.web.servlet.support.RequestContextUtils as RCU

import com.sun.org.apache.xalan.internal.xsltc.compiler.UseAttributeSets;

class TableTagLib extends BaseTagLib {
	static namespace = "bs"
	static final String COMPONENT_NAME = "table" 
	static final String COLUMN_COUNTER_PROPERTY = "columnCount"
	
	def table = {attrs, body ->
		def baseClass = (attrs.'class'?:'') + " table"
		
		if(Boolean.valueOf(attrs.remove('striped'))) {
			addToClass(attrs, "table-striped")
		}
		
		if(Boolean.valueOf(attrs.remove('bordered'))) {
			addToClass(attrs, "table-bordered")
		}
		
		if(Boolean.valueOf(attrs.remove('hover'))) {
			addToClass(attrs, "table-hover")
		}
		
		if(Boolean.valueOf(attrs.remove('condensed'))) {
			addToClass(attrs, "table-condensed")
		}
		
		defaultClasses(attrs, "table")
		
		def headers = attrs.remove('header')
		
		out << "<table " << outputAttributes(attrs, out) << ">" 
		pageScope."$COLUMN_COUNTER_PROPERTY" = 0;
		
		if(headers && !headers.empty && headers.size() > 0){
			out << "<thead>\n\t<tr>"
			for(header in headers.split(',')) {
				out << "\t\t<th>" << header << "</th>" 
			}
			out << "\t</tr>\n</thead>"
		}
		
		out << body()
		
		pageScope."$COLUMN_COUNTER_PROPERTY" = 0;
		
		out << '</table>'
	}
	
	def tableBody = {attrs, body ->
		def list = attrs.remove('in')
		def var = attrs.remove('var') ?: "bean"
		def status = attrs.remove('status') ?: "status"
		def noResultMessage = attrs.remove('noResultMessage')
		out << "<tbody " << outputAttributes(attrs, out) << ">"
		if(list && !list.empty) {
			
			int i = 0;
			for(bean in list) {
				out << body((var):bean, (status):i)
				i++
			}
			
		} else {
			def colspan = pageScope."$COLUMN_COUNTER_PROPERTY"
			if(noResultMessage) {
				out << tableBodyRow(null, "<td colspan=\"$colspan\">$noResultMessage</td>")	
			} else {
				out << tableBodyRow(null, "<td colspan=\"$colspan\"></td>")
			}
		}
		out << "</tbody>"
	}
	
	def tableBodyRow = {attrs, body->
		def baseClass = (attrs.'class'?:'') + (attrs.remove('decorate')?:'')
		
		baseClass = baseClass.trim()
		
		if(!baseClass.empty) {
			attrs.'class' = baseClass.trim()
		}
		
		out << '<tr ' << outputAttributes(attrs, out) << '/>'
		out << body()
		out << '</tr>'	
	}
	
	/**
	* Renders a sortable column to support sorting in list views.<br/>
	*
	* Attribute title or titleKey is required. When both attributes are specified then titleKey takes precedence,
	* resulting in the title caption to be resolved against the message source. In case when the message could
	* not be resolved, the title will be used as title caption.<br/>
	*
	* Examples:<br/>
	*
	* &lt;g:sortableColumn property="title" title="Title" /&gt;<br/>
	* &lt;g:sortableColumn property="title" title="Title" style="width: 200px" /&gt;<br/>
	* &lt;g:sortableColumn property="title" titleKey="book.title" /&gt;<br/>
	* &lt;g:sortableColumn property="releaseDate" defaultOrder="desc" title="Release Date" /&gt;<br/>
	* &lt;g:sortableColumn property="releaseDate" defaultOrder="desc" title="Release Date" titleKey="book.releaseDate" /&gt;<br/>
	*
	* @attr property - name of the property relating to the field
	* @attr defaultOrder default order for the property; choose between asc (default if not provided) and desc
	* @attr title title caption for the column
	* @attr titleKey title key to use for the column, resolved against the message source
	* @attr params a map containing request parameters
	* @attr action the name of the action to use in the link, if not specified the list action will be linked
	* @attr params A map containing URL query parameters
	* @attr class CSS class name
	*/
   Closure sortableColumn = {attrs, body->
	   pageScope."$COLUMN_COUNTER_PROPERTY" += 1;
	   def icon = (params.order == "asc" && attrs.property == params.sort) ? 
	   				icon(icon:'caret-up') : 
				   (
					   (params.order == "desc" && attrs.property == params.sort) ? 
					   icon(icon:'caret-down') : 
					   '' 
					)
	   
	   attrs.title = (attrs.title ? (attrs.title + ' ' + icon) : (body().trim()+ ' ' + icon)) 
	   
	   out << g.sortableColumn(attrs)
   }
   
   Closure headerColumn = {attrs, body->
	   pageScope."$COLUMN_COUNTER_PROPERTY" += 1;
	   def writer = out
		attrs.title = (attrs.title ? (attrs.title) : (body().trim()))
		
        if (!attrs.title && !attrs.titleKey) {
            throwTagError("Tag [sortableColumn] is missing required attribute [title] or [titleKey]")
        }

        // determine column title
        def title = attrs.remove("title")
        def titleKey = attrs.remove("titleKey")
        if (titleKey) {
            if (!title) title = titleKey
            def messageSource = grailsAttributes.messageSource
            def locale = RCU.getLocale(request)
            title = messageSource.getMessage(titleKey, null, title, locale)
        }

        writer << "<th "
        // process remaining attributes
        attrs.each { k, v ->
            writer << "${k}=\"${v?.encodeAsHTML()}\" "
        }
        writer << ">${title ?: body()}</th>"
   }
}
