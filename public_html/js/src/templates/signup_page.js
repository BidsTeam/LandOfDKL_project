define(function () { return function (__fest_context){"use strict";var __fest_self=this,__fest_buf="",__fest_chunks=[],__fest_chunk,__fest_attrs=[],__fest_select,__fest_if,__fest_iterator,__fest_to,__fest_fn,__fest_html="",__fest_blocks={},__fest_params,__fest_element,__fest_debug_file="",__fest_debug_line="",__fest_debug_block="",__fest_htmlchars=/[&<>"]/g,__fest_htmlchars_test=/[&<>"]/,__fest_short_tags = {"area":true,"base":true,"br":true,"col":true,"command":true,"embed":true,"hr":true,"img":true,"input":true,"keygen":true,"link":true,"meta":true,"param":true,"source":true,"wbr":true},__fest_element_stack = [],__fest_htmlhash={"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;"},__fest_jschars=/[\\'"\/\n\r\t\b\f<>]/g,__fest_jschars_test=/[\\'"\/\n\r\t\b\f<>]/,__fest_jshash={"\"":"\\\"","\\":"\\\\","/":"\\/","\n":"\\n","\r":"\\r","\t":"\\t","\b":"\\b","\f":"\\f","'":"\\'","<":"\\u003C",">":"\\u003E"},___fest_log_error;if(typeof __fest_error === "undefined"){___fest_log_error = (typeof console !== "undefined" && console.error) ? function(){return Function.prototype.apply.call(console.error, console, arguments)} : function(){};}else{___fest_log_error=__fest_error};function __fest_log_error(msg){___fest_log_error(msg+"\nin block \""+__fest_debug_block+"\" at line: "+__fest_debug_line+"\nfile: "+__fest_debug_file)}function __fest_replaceHTML(chr){return __fest_htmlhash[chr]}function __fest_replaceJS(chr){return __fest_jshash[chr]}function __fest_extend(dest, src){for(var i in src)if(src.hasOwnProperty(i))dest[i]=src[i];}function __fest_param(fn){fn.param=true;return fn}function __fest_call(fn, params,cp){if(cp)for(var i in params)if(typeof params[i]=="function"&&params[i].param)params[i]=params[i]();return fn.call(__fest_self,params)}function __fest_escapeJS(s){if (typeof s==="string") {if (__fest_jschars_test.test(s))return s.replace(__fest_jschars,__fest_replaceJS);} else if (typeof s==="undefined")return "";return s;}function __fest_escapeHTML(s){if (typeof s==="string") {if (__fest_htmlchars_test.test(s))return s.replace(__fest_htmlchars,__fest_replaceHTML);} else if (typeof s==="undefined")return "";return s;}__fest_buf+=("<div data-role=\"page\" id=\"signup-page\"><div class=\"menu-container\"><div class=\"menu-container__menu\"><div class=\"menu__menu-name\">Signup</div><form id=\"signup-form\" class=\"form-horizontal menu__form\" action=\"\/api\/auth\/signup\" method=\"POST\" enctype=\"application\/x-www-form-urlencoded\"><div class=\"menu-form__fields-container\"><div class=\"form-group menu-form__form-group\"><label for=\"signup-username-field\" class=\"control-label menu-form__field-name\">Username</label><input type=\"text\" name=\"username\" class=\"form-control menu-form__field\" id=\"signup-username-field\"/></div><div class=\"form-group form__form-group\"><label for=\"signup-email-field\" class=\"control-label menu-form__field-name\">E-mail</label><input type=\"text\" name=\"email\" class=\"form-control menu-form__field\" id=\"signup-email-field\"/></div><div class=\"form-group form__form-group\"><label for=\"signup-password-field\" class=\"control-label menu-form__field-name\">Пароль</label><input type=\"password\" name=\"password\" class=\"form-control menu-form__field\" id=\"signup-password-field\"/></div><div class=\"form-group form__form-group\"><label for=\"signup-password-repeat-field\" class=\"control-label menu-form__field-name\">Повторите пароль</label><input type=\"password\" name=\"repeat_password\" class=\"form-control menu-form__field\" id=\"signup-password-repeat-field\"/></div></div><div class=\"menu__button-container\"><input type=\"submit\" value=\"Зарегистрироваться\" class=\"btn btn-large btn-success menu__button\" disabled=\"disabled\"/></div><div class=\"menu__button-container\"><a href=\"#\" class=\"btn btn-large btn-primary menu__button\">Назад</a></div></form></div></div></div>");__fest_to=__fest_chunks.length;if (__fest_to) {__fest_iterator = 0;for (;__fest_iterator<__fest_to;__fest_iterator++) {__fest_chunk=__fest_chunks[__fest_iterator];if (typeof __fest_chunk==="string") {__fest_html+=__fest_chunk;} else {__fest_fn=__fest_blocks[__fest_chunk.name];if (__fest_fn) __fest_html+=__fest_call(__fest_fn,__fest_chunk.params,__fest_chunk.cp);}}return __fest_html+__fest_buf;} else {return __fest_buf;}} ; });