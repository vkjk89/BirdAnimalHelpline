version="$(date +%s)"
for i in `find $1 -name "*.html" | grep $2`; do sed  -i '' -e    "s;v=.*);v=$version);" ${i}; done