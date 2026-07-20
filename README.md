# kotoba-lang/css

Static CSS-in-Clojure with shadow-css-compatible class semantics and portable EDN sheets.

This is compatible with the small `kami.css` style DSL: maps render to
declarations, `:rules` render to selectors, `:keyframes` render to keyframes,
numbers become `px` except for unitless properties and zero, vectors become
space-joined CSS values, and keywords become bare CSS identifiers.

`kotoba.css.shadow/css` returns a source-position-derived class string. `css.build`
indexes those static forms, validates and deterministically emits targeted CSS with
optional minification. No browser runtime CSS generator is shipped.


## Maturity

| | |
|---|---|
| Role | ui-substrate |
| Tests | `clojure -M:test` |
| Operator console (UI/UX) | — |
| Export (CSV/JSON) | — |
| Shared CSS design system | yes (css.core/operator-theme) |
| Static source indexing | yes (`css.build/index-shadow-source`) |
| Targeted utility output | yes (`css.utility`) |
| JVM Clojure contract | CI tested |
| Node ClojureScript contract | CI tested with nbb |

## Compatibility boundary

- Static EDN sheets, declaration maps, keyframes, media rules and inline styles are stable.
- `kotoba.css.shadow/css` forms are build-time indexed; CSS generation is never shipped to the browser.
- Utility classes are a strict finite catalog. Unsupported utilities fail the strict build.
- Values and selectors that could terminate a declaration or rule are rejected.
- Dynamic styling uses conditional classes, inline style or CSS custom properties, not runtime rule generation.

## Test

```bash
clojure -M:test
clojure -M:lint
nbb test/run_nbb_contract_tests.cljs
```
