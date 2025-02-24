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
select count(distinct id) as "Quantia de episódios" from episodios where serie_id=1;

-- Apresenta a quantia de episódios totais de cada série do banco
SELECT s.id as "Identificador", s.titulo as "Título da série", COUNT(DISTINCT e.id) as "Quantia de episódios" 
FROM episodios e
JOIN serie s ON e.serie_id = s.id
GROUP BY s.id, s.titulo;

-- Apresenta os 10 mais bem avaliados
SELECT e.titulo, e.avaliacao FROM episodios e
JOIN serie s ON s.id = e.serie_id
WHERE e.avaliacao IS NOT NULL
AND s.titulo = 'The Witcher'
ORDER BY e.avaliacao DESC
LIMIT 10;

-- Apresentando o nome de todos os episodios de The Witcher
SELECT e.titulo FROM episodios e 
JOIN serie s ON s.id = e.serie_id
WHERE s.titulo = 'The Witcher';

-- Pesquisando um episódio de The Witcher que 
-- tenha a palavra Chaos no título
SELECT e.* FROM episodios e
JOIN serie s ON e.serie_id = s.id
WHERE s.titulo = 'The Witcher' 
AND e.titulo LIKE '%Chaos%' LIMIT 1;

-- Verifica se tinha episodio replicado (e tinha, aí decidi apagar todos)
SELECT e.id, e.titulo FROM episodios e JOIN serie s ON e.serie_id = s.id WHERE s.titulo = 'Game of Thrones' AND e.titulo = 'The Rains of Castamere';

-- Procurando quantia de eps duplicados
SELECT s.titulo AS "Título da Série", e.titulo AS "Título do Episódio", COUNT(*) AS "Quantia de Duplicados"
FROM episodios e
JOIN serie s ON e.serie_id = s.id
GROUP BY s.titulo, e.titulo
HAVING COUNT(*) > 1;

-- Deletando duplicados
DELETE FROM episodios e
WHERE e.id NOT IN (
    SELECT MIN(e.id) 
    FROM episodios e
    JOIN serie s ON e.serie_id = s.id
    GROUP BY e.serie_id, e.titulo
);

-- Inserindo novo ENUM na categoria
--ALTER TABLE serie DROP CONSTRAINT serie_categoria_check;

--ALTER TABLE serie ADD CONSTRAINT serie_categoria_check 
--CHECK (categoria IN ('ACAO', 'ROMANCE', 'COMEDIA', 'DRAMA', 'CRIME', 'ANIMACAO', 'DESCONHECIDO'));

