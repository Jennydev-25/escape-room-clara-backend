-- Roles de usuario (registro/login)

INSERT INTO roles (name)
SELECT v.c1
FROM (VALUES
  ('USER'),
  ('ADMIN')
) AS v(c1)
WHERE NOT EXISTS (SELECT 1 FROM roles);

-- Tipos de contacto (formulario de contacto)
INSERT INTO contact_types (name)
SELECT v.c1
FROM (VALUES
  ('QUESTION'),
  ('BUG'),
  ('SUGGESTION')
) AS v(c1)
WHERE NOT EXISTS (SELECT 1 FROM contact_types);

-- Carpetas del escritorio, contenido investigacion
INSERT INTO folders (code, name, min_chapter_to_open)
SELECT v.c1, v.c2, v.c3
FROM (VALUES
  ('leeme', 'Léeme', 1),
  ('tomas', 'Tomás', 2),
  ('fraude', 'Fraude', 3)
) AS v(c1, c2, c3)
WHERE NOT EXISTS (SELECT 1 FROM folders);

-- Puzzles: contenido del capitulo, pregunta y pistas
INSERT INTO puzzles (title, id_folder, type, min_chapter, display_order, question, hint1, hint2, hint3)
SELECT v.c1, v.c2, v.c3, v.c4, v.c5, v.c6, v.c7, v.c8, v.c9
FROM (VALUES
  ('Capítulo 1: La desaparición', (SELECT id FROM folders WHERE code = 'leeme'), 'SIMPLE_PASSWORD', 1, 1, 'Según la nota, ¿en qué no debes confiar nunca?', 'Vuelve a leer la nota con calma: no todas las frases cumplen la misma función.', 'Fíjate en la advertencia que habla de cómo valorar la información que encuentres.', 'La respuesta está en una frase intermedia de la nota, no en la última línea ni en el nombre de una carpeta.'),
  ('Capítulo 2: Tomás Elorza', (SELECT id FROM folders WHERE code = 'tomas'), 'CIPHER', 2, 1, 'Descifra el mensaje y escribe lo que has obtenido.', 'El texto del margen parece ilegible, pero conserva la estructura de una frase normal.', 'Busca una pista relacionada con un número de desplazamiento en el índice críptico. ¿Has oído hablar del código César?', 'Prueba a retroceder cuatro posiciones en el alfabeto para cada letra y conserva los espacios entre palabras.')
) AS v(c1, c2, c3, c4, c5, c6, c7, c8, c9)
WHERE NOT EXISTS (SELECT 1 FROM puzzles);

INSERT INTO puzzles (title, id_folder, type, min_chapter, display_order)
SELECT v.c1, v.c2, v.c3, v.c4, v.c5
FROM (VALUES
  ('Capítulo 3: El fraude', (SELECT id FROM folders WHERE code = 'tomas'), 'SIMPLE_CONTENT', 3, 2),
  ('Capítulo 3: El fraude', (SELECT id FROM folders WHERE code = 'fraude'), 'SIMPLE_CONTENT', 3, 1)
) AS v(c1, c2, c3, c4, c5)
WHERE NOT EXISTS (SELECT 1 FROM puzzles WHERE min_chapter = 3);

-- Archivos de cada puzzle en el orden en que se muestran
INSERT INTO puzzle_files (id_puzzle, path, display_order)
SELECT v.c1, v.c2, v.c3
FROM (VALUES
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 1: La desaparición'), '/leeme/leeme.txt', 1),
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 2: Tomás Elorza'), '/tomas/tomas_elorza.png', 1),
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 2: Tomás Elorza'), '/tomas/libreta_tomas.png', 2),
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 2: Tomás Elorza'), '/tomas/portada_conciliacion.png', 3),
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 2: Tomás Elorza'), '/tomas/indice_criptico.png', 4),
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 2: Tomás Elorza'), '/tomas/recorte_periodico.png', 5),
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 3: El fraude' AND id_folder = (SELECT id FROM folders WHERE code = 'tomas')), '/tomas/conciliacion_proveedores_2002-2005.pdf', 1),
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 3: El fraude' AND id_folder = (SELECT id FROM folders WHERE code = 'fraude')), '/fraude/factura_3812.png', 1),
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 3: El fraude' AND id_folder = (SELECT id FROM folders WHERE code = 'fraude')), '/fraude/factura_3847.png', 2),
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 3: El fraude' AND id_folder = (SELECT id FROM folders WHERE code = 'fraude')), '/fraude/factura_3911.png', 3),
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 3: El fraude' AND id_folder = (SELECT id FROM folders WHERE code = 'fraude')), '/fraude/factura_firmada.png', 4),
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 3: El fraude' AND id_folder = (SELECT id FROM folders WHERE code = 'fraude')), '/fraude/hoja_calculo_facturas.pdf', 5)
) AS v(c1, c2, c3)
WHERE NOT EXISTS (SELECT 1 FROM puzzle_files);

-- Respuestas esperadas de los puzzles con validación de contraseña/cifrado
INSERT INTO expected_answers (id_puzzle, answer_key, answer_hash)
SELECT v.c1, v.c2, v.c3
FROM (VALUES
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 1: La desaparición'), NULL, '$2b$10$TdOrxq3j7dOOf6cruaeomeZrDZe130XCxXk/dPLzAxEW6/UGo7QCa'),
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 2: Tomás Elorza'), NULL, '$2b$10$fodmKsv.mnlnPCzqSNFwqOpJLstQa5JOHR78.HKtIV9vvstNVxrta')
) AS v(c1, c2, c3)
WHERE NOT EXISTS (SELECT 1 FROM expected_answers);

-- Papelera:
INSERT INTO discarded_files (name, type, body, min_chapter, display_order)
SELECT v.c1, v.c2, v.c3, v.c4, v.c5
FROM (VALUES
  ('borrador_reportaje_puerto_v1.txt', 'TEXT', '/papelera/borrador_reportaje_puerto_v1.txt', 1, 1),
  ('notas_ayuntamiento_ronda.txt', 'TEXT', '/papelera/notas_ayuntamiento_ronda.txt', 1, 2),
  ('ideas_sueltas.txt', 'TEXT', '/papelera/ideas_sueltas.txt', 2, 1),
  ('contactos_pendientes.txt', 'TEXT', '/papelera/contactos_pendientes.txt', 3, 1),
  ('salgo_fatal.png', 'IMG', '/papelera/salgo_fatal.png', 1, 3)
) AS v(c1, c2, c3, c4, c5)
WHERE NOT EXISTS (SELECT 1 FROM discarded_files);

-- Chat: 4 contactos, 26 mensajes
INSERT INTO chat_messages (contact, character, content, sent_at, min_chapter, display_order)
SELECT v.c1, v.c2, v.c3, CAST(v.c4 AS TIMESTAMP), v.c5, v.c6
FROM (VALUES
  ('Redacción · El Faro', 'Javier', 'recordatorio: hoy es el cumple de Ibáñez, hay bizcocho en la sala de reuniones a las 12', '2025-06-10 10:48:00', 1, 1),
  ('Redacción · El Faro', 'Marina', '¿alguien tiene el cargador de USB-C? el mío ha muerto en combate', '2025-06-10 10:52:00', 1, 2),
  ('Redacción · El Faro', 'Javier', 'yo tengo, está en mi mesa, coge el que quieras', '2025-06-10 10:53:00', 1, 3),
  ('Redacción · El Faro', 'Marina', 'eres mi héroe', '2025-06-10 10:54:00', 1, 4),
  ('Redacción · El Faro', 'Fermín', 'como oiga la palabra bizcocho una vez más antes de las 12 me da algo', '2025-06-10 11:01:00', 1, 5),
  ('Redacción · El Faro', 'Javier', 'Fermín amargado como siempre jaja', '2025-06-10 11:02:00', 1, 6),
  ('Redacción · El Faro', 'Marina', 'Clara pareces zombi esta semana eh', '2025-06-10 11:15:00', 1, 7),
  ('Redacción · El Faro', 'Javier', 'es verdad, ayer casi te quedas dormida en la reunión de las 9', '2025-06-10 11:16:00', 1, 8),
  ('Redacción · El Faro', 'Clara', 'gracias por la preocupación jajaja estoy bien, solo cansada', '2025-06-10 11:20:00', 1, 9),
  ('Redacción · El Faro', 'Marina', 'duerme algo anda, que luego no hay café que te arregle', '2025-06-10 11:21:00', 1, 10),
  ('Mamá', 'Mamá', 'Hija, ¿este finde vienes a comer o otra vez tienes lío?', '2025-04-20 13:47:00', 1, 1),
  ('Mamá', 'Clara', 'Este finde no puedo mamá, tengo un cierre. El que viene seguro', '2025-04-20 14:10:00', 1, 2),
  ('Mamá', 'Mamá', 'Llevas semanas rara por teléfono. ¿Va todo bien de verdad?', '2025-06-08 20:05:00', 1, 3),
  ('Mamá', 'Clara', 'Sí mamá, tranquila, es solo trabajo. Te llamo este finde', '2025-06-08 21:32:00', 1, 4),
  ('Mamá', 'Mamá', 'Clara no me has llamado el finde pasado ni el anterior. Contesta cuando puedas, aunque sea un mensaje', '2025-06-29 19:15:00', 1, 5),
  ('Diego Barreda', 'Clara', 'Buenas tardes, Sr. Barreda. Soy Clara Vega, de El Faro de San Adrián. Estoy preparando un reportaje sobre el cierre de la antigua conservera Marlecha y me gustaría hacerle un par de preguntas.', '2025-05-02 10:14:00', 1, 1),
  ('Diego Barreda', 'Diego', 'Fue hace veinte años. No creo que tenga mucho que aportar que no esté ya en la prensa de la época.', '2025-05-02 18:42:00', 1, 2),
  ('Diego Barreda', 'Clara', 'Solo dos preguntas más: ¿por qué se descartó parte del testimonio de Elena Sáez? ¿Y qué relación tiene usted con Norte Atlántico Servicios?', '2025-06-06 12:20:00', 1, 3),
  ('Diego Barreda', 'Diego', 'Le agradecería que tuviera cuidado con las preguntas que hace y a quién se las hace. Marlecha es historia cerrada.', '2025-06-09 09:07:00', 1, 4),
  ('Andrés Salas', 'Clara', 'Andrés Salas, ¿verdad? He encontrado su nombre en unas notas de Tomás Elorza de 2005. ¿Qué pasó con la fotografía 17?', '2025-06-10 17:30:00', 9, 1),
  ('Andrés Salas', 'Andrés', '¿Quién le ha dado mi nombre?', '2025-06-15 22:14:00', 9, 2),
  ('Andrés Salas', 'Clara', 'Nadie. Lo encontré yo. Lleva veinte años sin salir a la luz y creo que usted lo sabe.', '2025-06-15 22:19:00', 9, 3),
  ('Andrés Salas', 'Andrés', 'No me crea. Compruébelo todo usted misma, con sus propias fuentes. Yo solo le voy a dar piezas, nunca la respuesta entera.', '2025-06-24 08:51:00', 9, 4),
  ('Andrés Salas', 'Andrés', 'He encontrado algo que Tomás dejó atrás.', '2025-07-05 21:03:00', 9, 5),
  ('Andrés Salas', 'Clara', '¿Qué es?', '2025-07-05 21:04:00', 9, 6),
  ('Andrés Salas', 'Andrés', 'No puedo explicárselo por aquí. Quedamos en persona.', '2025-07-05 21:09:00', 9, 7)
) AS v(c1, c2, c3, c4, c5, c6)
WHERE NOT EXISTS (SELECT 1 FROM chat_messages);

-- Correos: 4 contactos, 4 mensajes
INSERT INTO emails (sender, recipient, subject, sent_date, body, min_chapter, display_order)
SELECT v.c1, v.c2, v.c3, CAST(v.c4 AS DATE), v.c5, v.c6, v.c7
FROM (VALUES
  ('Marina Ortiz', 'Clara Vega', 'Lo de esta noche', '2025-06-03', 'Clara, ¿vienes luego a la despedida de Fermín? Lleva 31 años en el Faro y al final le vamos
a hacer algo en el bar de siempre, nada del otro mundo. Dile que sí aunque sea media hora,
que si no se va a poner triste y luego nos toca aguantarlo un mes.

Por cierto, ¿al final entregaste lo del puerto? Ibáñez ha preguntado dos veces esta mañana.

M.', 1, 1),
  ('Recursos Humanos — El Faro de San Adrián', 'Clara Vega', 'Recordatorio: revisión de días de vacaciones pendientes', '2025-06-05', 'Estimada Clara Vega:

Le recordamos que, según nuestros registros, tiene 6 días de vacaciones del año en curso
todavía sin planificar. Le rogamos que lo comunique al departamento antes de fin de mes
para evitar la pérdida de los mismos según el convenio vigente.

Un saludo,
Recursos Humanos', 1, 2),
  ('Marina Ortiz', 'Clara Vega', '¿Todo bien?', '2025-06-26', 'Oye, te noto rara últimamente. Llevas dos semanas comiendo sola en la mesa del rincón con
el portátil y contestando por monosílabos. Si es por lo del puerto, tranquila, a Ibáñez se
le olvida todo en cuanto sale algo nuevo.

Si es otra cosa y no quieres hablar de ello, vale, pero al menos dime que estás bien.

M.', 2, 1),
  ('Registro Mercantil de Asturias', 'Clara Vega', 'Confirmación de solicitud — Expte. 2025-4471', '2025-06-27', 'Estimada Sra. Vega,

Confirmamos la recepción de su solicitud de información registral (Expte. 2025-4471),
relativa al reportaje sobre concesiones del puerto pesquero que nos indica en su instancia.
El plazo estimado de respuesta es de 5 a 7 días hábiles.

Atentamente,
Registro Mercantil de Asturias', 2, 2)
) AS v(c1, c2, c3, c4, c5, c6, c7)
WHERE NOT EXISTS (SELECT 1 FROM emails);
