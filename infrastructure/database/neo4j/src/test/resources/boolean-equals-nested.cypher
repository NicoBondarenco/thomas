CREATE (e:BooleanProps {id: '78549195-822d-46a6-87c7-137a5e43351a', prop_boolean: true});
CREATE (e:BooleanProps {id: '95a34b09-6b6c-4f67-af40-9b9d0029abd6', prop_boolean: true});
CREATE (e:BooleanProps {id: 'cd4101ac-0d7c-4d57-9b21-aa6d03c7a33d', prop_boolean: false});

MATCH (o:BooleanProps {id: '78549195-822d-46a6-87c7-137a5e43351a'}) CREATE (e:BooleanNestedProps {id: 'fc7a2d80-8da1-4721-859c-a4b8d222f7f9', prop_boolean: false})-[:BOOLEAN_NESTED]->(o);
MATCH (o:BooleanProps {id: '95a34b09-6b6c-4f67-af40-9b9d0029abd6'}) CREATE (e:BooleanNestedProps {id: 'd8247938-193c-4013-944b-baf952193508', prop_boolean: true})-[:BOOLEAN_NESTED]->(o);
MATCH (o:BooleanProps {id: 'cd4101ac-0d7c-4d57-9b21-aa6d03c7a33d'}) CREATE (e:BooleanNestedProps {id: '6c379fd8-efe7-48b6-af9c-aea4c2e4af08', prop_boolean: true})-[:BOOLEAN_NESTED]->(o);