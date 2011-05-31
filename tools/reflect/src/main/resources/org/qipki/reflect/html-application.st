<!DOCTYPE html>
<html>
<head>
<title>$app.name$ Structure</title>
<style>
div.qi4j-application ul {
    margin: 0;
    padding: 0;
    decoration: none;
}
div.qi4j-application ul.qi4j-layers {
    border: 1px solid black;
}
div.qi4j-application li {
    border: 1px solid gray;
    margin: 0.5em;
    padding: 0.5em;
    list-style-type: none;
}
div.qi4j-application li:before {
    font-weight: bold;
}
div.qi4j-application li.qi4j-layer:before {
    content: 'Layer: ';
}
div.qi4j-application li.qi4j-module:before {
    content: 'Module: ';
}
div.qi4j-application li.qi4j-object:before {
    content: 'Object: ';
}
div.qi4j-application li.qi4j-transient:before {
    content: 'Transient: ';
}
div.qi4j-application li.qi4j-value:before {
    content: 'Value: ';
}
div.qi4j-application li.qi4j-entity:before {
    content: 'Entity: ';
}
div.qi4j-application li.qi4j-service:before {
    content: 'Service: ';
}
div.qi4j-application li p {
    font-size: 0.8em;
    padding: 0; margin: 0 0 0 0.5em;
}
</style>
</head>
<body>

<div class="qi4j-application">

    <h1>$app.name$ Structure</h1>

    <ul class="qi4j-layers">
    $app.layers:{ l |
        <li class="qi4j-layer">$l.name$
            $if(l.hasUsedLayers)$ <p>Use Layer(s) $l.usedLayers; separator=", "$</p> $endif$
            $if(l.hasModules)$
                <ul class="qi4j-modules">
                $l.modules:{ m |
                    <li class="qi4j-module">$m.name$
                        $if(m.hasObjects)$
                            <ul class="qi4j-objects">
                            $m.objects:{ o |
                                <li class="qi4j-object">Object $o.type.simpleName$ with $o.visibility$ visibility</li>
                            }$
                            </ul>
                        $endif$
                        $if(m.hasTransients)$
                            <ul class="qi4j-transients">
                            $m.transients:{ t |
                                <li class="qi4j-transient">Transient of type $t.type.simpleName$ with $t.visibility$ visibility
                                    <p>Mixin Types are $t.mixinTypes:{ mt | $mt.simpleName$ }$</p>
                                </li>
                            }$
                            </ul>
                        $endif$
                        $if(m.hasValues)$
                            <ul class="qi4j-values">
                            $m.values:{ v |
                                <li class="qi4j-value">Value of type $v.type.simpleName$ with $v.visibility$ visibility
                                    <p>Mixin Types are $v.mixinTypes:{ mt | $mt.simpleName$ }$</p>
                                </li>
                            }$
                            </ul>
                        $endif$
                        $if(m.hasEntities)$
                            <ul class="qi4j-entities">
                            $m.entities:{ e |
                                <li class="qi4j-entity">Entity of type $e.type.simpleName$ with $e.visibility$ visibility
                                    <p>Mixin Types are $e.mixinTypes:{ mt | $mt.simpleName$ }$</p>
                                </li>
                            }$
                            </ul>
                        $endif$
                        $if(m.hasServices)$
                            <ul class="qi4j-services">
                            $m.services:{ s |
                                <li class="qi4j-service">Service of type $s.type.simpleName$ identified by $s.identity$ with $s.visibility$ visibility
                                    <p>Mixin Types are $s.mixinTypes:{ mt | $mt.simpleName$ }$</p>
                                    $if(s.configurationType)$ <p>Configuration Type is $s.configurationType.simpleName$</p> $endif$
                                    <p>Instanciated on startup? $s.instanciateOnStartup$</p>
                                    <p>This service was imported? $s.imported$</p>
                                </li>
                            }$
                            </ul>
                        $endif$
                    </li>
                }$
                </ul>
            $endif$
        </li>
    }$
    </ul>
</div>

</body>
</html>
