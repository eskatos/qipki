+------------------------------------------------------------------------------
| <app.name> Structure
+------------------------------------------------------------------------------
<app.layers:{ l | |
| Layer <l.name>
<if(l.hasUsedLayers)>|   Use Layer(s) <l.usedLayers; separator=", ">
<endif><if(l.hasModules)><l.modules:{ m | |
|   Module <m.name>
<if(m.hasObjects)><m.objects:{ o | |
|     Object <o.type.simpleName> with <o.visibility> visibility
}><endif><if(m.hasTransients)><m.transients:{ t | |
|     Transient of type <t.type.simpleName> with <t.visibility> visibility
|       Mixin Types are <t.mixinTypes:{ mt | <mt.simpleName> }>
}><endif><if(m.hasValues)><m.values:{ v | |
|     Value of type <v.type.simpleName> with <v.visibility> visibility
|       Mixin Types are <v.mixinTypes:{ mt | <mt.simpleName> }>
}><endif><if(m.hasEntities)><m.entities:{ e | |
|     Entity of type <e.type.simpleName> with <e.visibility> visibility
|       Mixin Types are <e.mixinTypes:{ mt | <mt.simpleName> }>
}><endif><if(m.hasServices)><m.services:{ s | |
|     Service of type <s.type.simpleName> identified by <s.identity> with <s.visibility> visibility
<if(s.configurationType)>|       Configuration Type is <s.configurationType.simpleName>
<endif>|       Instanciated on startup? <s.instanciateOnStartup>
|       This service was imported? <s.imported>
}><endif>}><endif>}>|
+------------------------------------------------------------------------------


