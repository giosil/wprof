@echo off

echo Clean wux folder..
del /Q .\wux\*.*

echo Compile WUX...
call tsc --declaration --project ./ts/wux/tsconfig.json

echo Compile wprof...
call tsc --noEmitHelpers --declaration --project ./ts/wprof/tsconfig.json

rem Install first https://www.npmjs.com/package/minifier
echo Minify...
call minify ./wux/js/wux.js
call minify ./wux/js/wprof.js

rem Install first https://www.npmjs.com/package/uglify-js
rem uglifyjs -c -o ./wux/js/wux.min.js -m -- ./wux/js/wux.js
rem uglifyjs -c -o ./wux/js/cloudfarma.min.js -m -- ./wux/js/cloudfarma.js