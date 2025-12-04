import json

with open('lec-06-prg-03-json-example.json', 'r') as json_in_file:
    superHeroes = json.load(json_in_file)
    print(superHeroes['homeTown'])
    print(superHeroes['active'])
    print(superHeroes['members'][1]['powers'][2])
