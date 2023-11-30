# shellcheck disable=SC2012
# shellcheck disable=SC2035
# shellcheck disable=SC2162
ls -1 /stubs/*.json | sed 's/.json$//' | while read col; do
    echo "$col";
    mongoimport --db "storebuilder-test" --collection "$col" --file "$col.json" --jsonArray --parseGrace "skip";
done