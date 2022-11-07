#!/bin/bash

cd ./src/main/webapp

echo "Clean dist folder.."
rm -fr ./wux

echo "Compile WUX..."
tsc --declaration --project ./ts/wux/tsconfig.json

echo "Compile wprof..."
tsc --declaration --project ./ts/wprof/tsconfig.json

echo "Minify..."

# Install first https://www.npmjs.com/package/minifier
minify ./wux/js/wux.js
minify ./wux/js/wprof.js

# Install first https://www.npmjs.com/package/uglify-js
# Usage: uglifyjs input_file -c (compress) -o (output_file) output_file
# uglifyjs ./wux/js/wux.js -c -o ./wux/js/wux.min.js
# uglifyjs ./wux/js/wprof.js -c -o ./wux/js/wprof.min.js

