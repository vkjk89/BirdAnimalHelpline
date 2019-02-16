version="$(date +%s)"
echo $1
echo $2
for i in `find $1 -name "*.html" | grep $2`; do sed  -i '' -e    "s;v=.*);v=$version);" ${i}; done