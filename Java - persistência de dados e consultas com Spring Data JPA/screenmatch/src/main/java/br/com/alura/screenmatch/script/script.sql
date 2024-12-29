-- Apresenta todas as series por ordem alfabética
select 
id as "Identificador",
titulo as "Título da série",
atores as "Atores",
avaliacao as "Avaliação",
categoria as "Categoria",
poster as "Link do poster"
from serie ORDER BY titulo;

-- Apresenta todos os episódios da série de id=6
select count(distinct id) as "Quantia de episódios" from episodios where serie_id=6;

-- Apresenta a quantia de episódios totais de cada série do banco
SELECT s.id as "Identificador", s.titulo as "Título da série", COUNT(DISTINCT e.id) as "Quantia de episódios" 
FROM episodios e
JOIN serie s ON e.serie_id = s.id
GROUP BY s.id, s.titulo;


