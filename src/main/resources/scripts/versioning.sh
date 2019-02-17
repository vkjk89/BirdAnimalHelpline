version="$(date +%s)"
echo "Vimal started versioning"
pwd
hostname -f
id
ls -lrt
ls -lrt $1
echo $1
echo "Vimal versioning done"
find $1 -name "*.html"
for i in `find $1 -name "*.html"`; do echo $i; sed -i "s;v=.*);v=$version);" ${i}; done