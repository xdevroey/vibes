#!/bin/bash -e

# Bash script
# 
# author: Xavier Devroey

if [ "$3" == "" ] ; then
    echo "Usage : $0 <bfsheight> <input.ts> <output-dir>";
    echo "Exemple : $0 3 svm.ts slices out/";
    exit 1;
fi;

scriptdir=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

bfsheight=$1
ts=$2
outputdir=$3
slicedts=$outputdir/$(basename $ts | sed s'/.ts//')-sliced-$bfsheight.ts
fronttimbukts=$slicedts.frontaccept.txt
fronttimbukstatesmap=$slicedts.frontaccept.states.csv
fronttimbukactionsmap=$slicedts.frontaccept.actions.csv
alltimbukts=$slicedts.allaccept.txt
alltimbukstatesmap=$slicedts.allaccept.states.csv
alltimbukactionsmap=$slicedts.allaccept.actions.csv
toolbox=$scriptdir/toolbox-transformation-1.1.5-SNAPSHOT-jar-with-dependencies.jar

mkdir -p $outputdir


echo "Slicing $ts to $slicedts"
java -jar $toolbox -lts $ts -out $slicedts -bfshslicer $bfsheight


acceptstates=$(cat $slicedts | grep '<!--' | sed s'/<!--//' | sed s'/-->//')

echo "Producing Timbuk for $slicedts to $fronttimbukts"
echo "Accepting states will be $acceptstates"

java -jar $toolbox -lts $slicedts -out $fronttimbukts -timbuk "$acceptstates" $ts $fronttimbukstatesmap $fronttimbukactionsmap

echo "Producing Timbuk for $slicedts to $alltimbukts"
echo "Accepting states will be all"

java -jar $toolbox -lts $slicedts -out $alltimbukts -timbuk all $ts $alltimbukstatesmap $alltimbukactionsmap

