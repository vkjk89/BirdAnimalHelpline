version="$(date +%s)"
echo "Vimal"
pwd
hostname -f
#id
ls -lrt
ls -lrt $1
ls -lrt $2
echo $1
echo $2
echo "Vimal done"
find $1 -name "*.html"
for i in `find $1 -name "*.html"`; do echo $i; sed  -i '' -e    "s;v=.*);v=$version);" ${i}; done