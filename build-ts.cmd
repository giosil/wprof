@echo off

cd src\main\webapp

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
rem Usage: uglifyjs input_file -c (compress) -o (output_file) output_file
rem call uglifyjs ./wux/js/wux.js -c -o ./wux/js/wux.min.js
rem call uglifyjs ./wux/js/wprof.js -c -o ./wux/js/wprof.min.js
