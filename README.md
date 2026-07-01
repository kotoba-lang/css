# kotoba-lang/css

CSS as EDN data.

This is compatible with the small `kami.css` style DSL: maps render to
declarations, `:rules` render to selectors, `:keyframes` render to keyframes,
numbers become `px` except for unitless properties and zero, vectors become
space-joined CSS values, and keywords become bare CSS identifiers.
