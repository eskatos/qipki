<!DOCTYPE html>
<html>
<head>
<title>$app.name$ Structure</title>
<style>
ul {
    border: 1px solid black;
    margin: 8px;
    padding: 4px;
    decoration: none;
}
li {
    border: 1px solid gray;
    margin: 4px;
    padding: 4px;
    list-style-type: none;
}
</style>
</head>
<body>

<h1>$app.name$ Structure</h1>

<ul class="layers">
$app.layers:{ l |
    <li class="layer">$l.name$
        $if(l.hasUsedLayers)$ <p>Use Layer(s) $l.usedLayers; separator=", "$</p> $endif$
        $if(l.hasModules)$
            <ul class="modules">
            $l.modules:{ m |
                <li class="module">$m.name$
                    $if(m.hasObjects)$
                        <ul class="objects">
                        $m.objects:{ o |
                            <li class="object">Object $o.type.simpleName$ with $o.visibility$ visibility</li>
                        }$
                        </ul>
                    $endif$
                    $if(m.hasTransients)$
                        <ul class="transients">
                        $m.transients:{ t |
                            <li class="transient">Transient of type $t.type.simpleName$ with $t.visibility$ visibility
                                <p>Mixin Types are $t.mixinTypes:{ mt | $mt.simpleName$ }$</p>
                            </li>
                        }$
                        </ul>
                    $endif$
                    $if(m.hasValues)$
                        <ul class="values">
                        $m.values:{ v |
                            <li class="value">Value of type $v.type.simpleName$ with $v.visibility$ visibility
                                <p>Mixin Types are $v.mixinTypes:{ mt | $mt.simpleName$ }$</p>
                            </li>
                        }$
                        </ul>
                    $endif$
                    $if(m.hasEntities)$
                        <ul class="entities">
                        $m.entities:{ e |
                            <li class="entity">Entity of type $e.type.simpleName$ with $e.visibility$ visibility
                                <p>Mixin Types are $e.mixinTypes:{ mt | $mt.simpleName$ }$</p>
                            </li>
                        }$
                        </ul>
                    $endif$
                    $if(m.hasServices)$
                        <ul class="services">
                        $m.services:{ s |
                            <li class="service">Service of type $s.type.simpleName$ identified by $s.identity$ with $s.visibility$ visibility
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

</body>
</html>
