/*
Copyright (c) 2010, Yahoo! Inc. All rights reserved.
Code licensed under the BSD License:
http://developer.yahoo.com/yui/license.html
version: 3.2.0
build: nightly
*/
YUI.add("cache-base",function(D){var A=D.Lang,B=D.Lang.isDate,C=function(){C.superclass.constructor.apply(this,arguments);};D.mix(C,{NAME:"cache",ATTRS:{max:{value:0,setter:"_setMax"},size:{readOnly:true,getter:"_getSize"},uniqueKeys:{value:false},expires:{value:0,validator:function(E){return D.Lang.isDate(E)||(D.Lang.isNumber(E)&&E>=0);}},entries:{readOnly:true,getter:"_getEntries"}}});D.extend(C,D.Base,{_entries:null,initializer:function(E){this.publish("add",{defaultFn:this._defAddFn});this.publish("flush",{defaultFn:this._defFlushFn});this._entries=[];},destructor:function(){this._entries=[];},_setMax:function(F){var E=this._entries;if(F>0){if(E){while(E.length>F){E.shift();}}}else{F=0;this._entries=[];}return F;},_getSize:function(){return this._entries.length;},_getEntries:function(){return this._entries;},_defAddFn:function(H){var F=this._entries,E=this.get("max"),G=H.entry;if(this.get("uniqueKeys")&&(this.retrieve(H.entry.request))){F.shift();}while(E&&F.length>=E){F.shift();}F[F.length]=G;},_defFlushFn:function(E){this._entries=[];},_isMatch:function(F,E){if(!E.expires||new Date()<E.expires){return(F===E.request);}return false;},add:function(G,F){var E=this.get("expires");if(this.get("initialized")&&((this.get("max")===null)||this.get("max")>0)&&(A.isValue(G)||A.isNull(G)||A.isUndefined(G))){this.fire("add",{entry:{request:G,response:F,cached:new Date(),expires:B(E)?E:(E?new Date(new Date().getTime()+this.get("expires")):null)}});}else{}},flush:function(){this.fire("flush");},retrieve:function(I){var E=this._entries,H=E.length,G=null,F=H-1;if((H>0)&&((this.get("max")===null)||(this.get("max")>0))){this.fire("request",{request:I});for(;F>=0;F--){G=E[F];if(this._isMatch(I,G)){this.fire("retrieve",{entry:G});if(F<H-1){E.splice(F,1);E[E.length]=G;}return G;}}}return null;}});D.Cache=C;},"3.2.0",{requires:["base"]});YUI.add("cache-offline",function(E){function D(){D.superclass.constructor.apply(this,arguments);}var A=E.config.win.localStorage,C=E.JSON,F={NAME:"cacheOffline",ATTRS:{sandbox:{value:"default",writeOnce:"initOnly"},expires:{value:86400000},max:{value:null,readOnly:true},uniqueKeys:{value:true,readOnly:true,setter:function(){return true;}}},flushAll:function(){var G=A,H;if(G){if(G.clear){G.clear();}else{for(H in G){if(G.hasOwnProperty(H)){G.removeItem(H);delete G[H];}}}}else{}}},B=A?{_setMax:function(G){return null;},_getSize:function(){var I=0,H=0,G=A.length;for(;H<G;++H){if(A.key(H).indexOf(this.get("sandbox"))===0){I++;}}return I;},_getEntries:function(){var G=[],J=0,I=A.length,H=this.get("sandbox");for(;J<I;++J){if(A.key(J).indexOf(H)===0){G[J]=C.parse(A.key(J).substring(H.length));}}return G;},_defAddFn:function(L){var K=L.entry,J=K.request,I=K.cached,G=K.expires;K.cached=I.getTime();K.expires=G?G.getTime():G;try{A.setItem(this.get("sandbox")+C.stringify({"request":J}),C.stringify(K));}catch(H){this.fire("error",{error:H});}},_defFlushFn:function(I){var H,G=A.length-1;for(;G>-1;--G){H=A.key(G);if(H.indexOf(this.get("sandbox"))===0){A.removeItem(H);}}},retrieve:function(J){this.fire("request",{request:J});var I,G,H;try{H=this.get("sandbox")+C.stringify({"request":J});try{I=C.parse(A.getItem(H));}catch(L){}}catch(K){}if(I){I.cached=new Date(I.cached);G=I.expires;G=!G?null:new Date(G);I.expires=G;if(this._isMatch(J,I)){this.fire("retrieve",{entry:I});return I;}}return null;}}:{_setMax:function(G){return null;}};E.mix(D,F);E.extend(D,E.Cache,B);E.CacheOffline=D;},"3.2.0",{requires:["cache-base","json"]});YUI.add("cache-plugin",function(B){function A(E){var D=E&&E.cache?E.cache:B.Cache,F=B.Base.create("dataSourceCache",D,[B.Plugin.Base]),C=new F(E);F.NS="tmpClass";return C;}B.mix(A,{NS:"cache",NAME:"cachePlugin"});B.namespace("Plugin").Cache=A;},"3.2.0",{requires:["cache-base"]});YUI.add("cache",function(A){},"3.2.0",{use:["cache-base","cache-offline","cache-plugin"]});