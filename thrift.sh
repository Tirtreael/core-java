#!/bin/bash
cd "$(dirname "$0")"

if [ $# -ne 1 ] || [ ! -d "$1"/ignis/rpc/ ] || [ ! -f $(find "$1"/ignis/rpc/ -name "*thrift" | head -n 1) ]; 
    then echo "usage thrift.sh <rpc-folder>"
    exit
fi


out="src/main/java/"
rm -fr "$out/org/ignis/rpc"
for file in `find $1/ignis -name "*thrift"`; do
    thrift --gen java:private-members,generated_annotations=suppress -out $out $file &
done
wait
