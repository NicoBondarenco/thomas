CREATE (e:StringProps {id: '38e93cf3-ce63-4759-a2af-57c3113a2267', prop_name: 'Antonia Batista'});
CREATE (e:StringProps {id: '2ed8297a-e2bd-4bcd-b229-8f0da72467b0', prop_name: 'Antônia Batista'});
CREATE (e:StringProps {id: '08141fdc-9c1e-4d95-9e23-6d96d9c1ec8e', prop_name: 'antônia batista'});

MATCH (o:StringProps {id: '38e93cf3-ce63-4759-a2af-57c3113a2267'}) CREATE (e:StringNestedProps {id: '5f510e8f-f429-4b07-aa21-a94eef462a50'})-[:STRING_NESTED]->(o);
MATCH (o:StringProps {id: '2ed8297a-e2bd-4bcd-b229-8f0da72467b0'}) CREATE (e:StringNestedProps {id: 'ae5f0143-dbec-499e-9f24-f7db86b89562'})-[:STRING_NESTED]->(o);
MATCH (o:StringProps {id: '08141fdc-9c1e-4d95-9e23-6d96d9c1ec8e'}) CREATE (e:StringNestedProps {id: 'd104a2ed-d4b2-4a45-b13d-2bb335f2fd15'})-[:STRING_NESTED]->(o);
