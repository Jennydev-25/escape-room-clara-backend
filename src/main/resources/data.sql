-- Roles de usuario (registro/login)
INSERT INTO roles (name)
VALUES
  ('USER'),
  ('ADMIN');

-- Tipos de contacto (formulario de contacto)
INSERT INTO contact_types (name)
VALUES
  ('QUESTION'),
  ('BUG'),
  ('SUGGESTION');

-- Carpetas del escritorio, contenido investigacion
INSERT INTO folders (code, name, min_chapter_to_open)
VALUES
  ('leeme', 'Léeme', 1),
  ('tomas', 'Tomás', 2),
  ('fraude', 'Fraude', 3);

-- Puzzles: contenido del capitulo, pregunta y pistas
INSERT INTO puzzles (title, id_folder, type, min_chapter, display_order, question, hint1, hint2, hint3)
VALUES
  ('Capítulo 1: La desaparición', (SELECT id FROM folders WHERE code = 'leeme'), 'SIMPLE_PASSWORD', 1, 1, 'Según la nota, ¿en qué no debes confiar nunca?', 'Vuelve a leer la nota con calma: no todas las frases cumplen la misma función.', 'Fíjate en la advertencia que habla de cómo valorar la información que encuentres.', 'La respuesta está en una frase intermedia de la nota, no en la última línea ni en el nombre de una carpeta.'),
  ('Capítulo 2: Tomás Elorza', (SELECT id FROM folders WHERE code = 'tomas'), 'CIPHER', 2, 1, 'Descifra el mensaje y escribe lo que has obtenido.', 'El texto del margen parece ilegible, pero conserva la estructura de una frase normal.', 'Busca una pista relacionada con un número de desplazamiento en el índice críptico. ¿Has oído hablar del código César?', 'Prueba a retroceder cuatro posiciones en el alfabeto para cada letra y conserva los espacios entre palabras.');

INSERT INTO puzzles (title, id_folder, type, min_chapter, display_order)
VALUES
  ('Capítulo 3: El fraude', (SELECT id FROM folders WHERE code = 'tomas'), 'SIMPLE_CONTENT', 3, 2),
  ('Capítulo 3: El fraude', (SELECT id FROM folders WHERE code = 'fraude'), 'SIMPLE_CONTENT', 3, 1);

-- Archivos de cada puzzle (una fila por ruta en static/), en el orden en que se muestran
INSERT INTO puzzle_files (id_puzzle, path, display_order)
VALUES
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
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 3: El fraude' AND id_folder = (SELECT id FROM folders WHERE code = 'fraude')), '/fraude/hoja_calculo_facturas.pdf', 5);

-- Respuestas esperadas de los puzzles con validación de contraseña/cifrado
INSERT INTO expected_answers (id_puzzle, answer_key, answer_hash)
VALUES
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 1: La desaparición'), NULL, '$2b$10$TdOrxq3j7dOOf6cruaeomeZrDZe130XCxXk/dPLzAxEW6/UGo7QCa'),
  ((SELECT id FROM puzzles WHERE title = 'Capítulo 2: Tomás Elorza'), NULL, '$2b$10$fodmKsv.mnlnPCzqSNFwqOpJLstQa5JOHR78.HKtIV9vvstNVxrta');

-- Papelera:
INSERT INTO discarded_files (name, type, body, min_chapter)
VALUES
  ('borrador_reportaje_puerto_v1.txt', 'TEXT', '/papelera/borrador_reportaje_puerto_v1.txt', 1),
  ('notas_ayuntamiento_ronda.txt', 'TEXT', '/papelera/notas_ayuntamiento_ronda.txt', 1),
  ('ideas_sueltas.txt', 'TEXT', '/papelera/ideas_sueltas.txt', 2),
  ('contactos_pendientes.txt', 'TEXT', '/papelera/contactos_pendientes.txt', 3),
  ('salgo_fatal.png', 'IMG', '/papelera/salgo_fatal.png', 1);

-- Chat: 4 contactos, 26 mensajes
INSERT INTO chat_messages (contact, character, content, min_chapter, display_order)
VALUES
  ('Redacción · El Faro', 'Javier', 'recordatorio: hoy es el cumple de Ibáñez, hay bizcocho en la sala de reuniones a las 12', 1, 1),
  ('Redacción · El Faro', 'Marina', '¿alguien tiene el cargador de USB-C? el mío ha muerto en combate', 1, 2),
  ('Redacción · El Faro', 'Javier', 'yo tengo, está en mi mesa, coge el que quieras', 1, 3),
  ('Redacción · El Faro', 'Marina', 'eres mi héroe', 1, 4),
  ('Redacción · El Faro', 'Fermín', 'como oiga la palabra bizcocho una vez más antes de las 12 me da algo', 1, 5),
  ('Redacción · El Faro', 'Javier', 'Fermín amargado como siempre jaja', 1, 6),
  ('Redacción · El Faro', 'Marina', 'Clara pareces zombi esta semana eh', 1, 7),
  ('Redacción · El Faro', 'Javier', 'es verdad, ayer casi te quedas dormida en la reunión de las 9', 1, 8),
  ('Redacción · El Faro', 'Clara', 'gracias por la preocupación jajaja estoy bien, solo cansada', 1, 9),
  ('Redacción · El Faro', 'Marina', 'duerme algo anda, que luego no hay café que te arregle', 1, 10),
  ('Mamá', 'Mamá', 'Hija, ¿este finde vienes a comer o otra vez tienes lío?', 1, 1),
  ('Mamá', 'Clara', 'Este finde no puedo mamá, tengo un cierre. El que viene seguro', 1, 2),
  ('Mamá', 'Mamá', 'Llevas semanas rara por teléfono. ¿Va todo bien de verdad?', 1, 3),
  ('Mamá', 'Clara', 'Sí mamá, tranquila, es solo trabajo. Te llamo este finde', 1, 4),
  ('Mamá', 'Mamá', 'Clara no me has llamado el finde pasado ni el anterior. Contesta cuando puedas, aunque sea un mensaje', 1, 5),
  ('Diego Barreda', 'Clara', 'Buenas tardes, Sr. Barreda. Soy Clara Vega, de El Faro de San Adrián. Estoy preparando un reportaje sobre el cierre de la antigua conservera Marlecha y me gustaría hacerle un par de preguntas.', 1, 1),
  ('Diego Barreda', 'Diego', 'Fue hace veinte años. No creo que tenga mucho que aportar que no esté ya en la prensa de la época.', 1, 2),
  ('Diego Barreda', 'Clara', 'Solo dos preguntas más: ¿por qué se descartó parte del testimonio de Elena Sáez? ¿Y qué relación tiene usted con Norte Atlántico Servicios?', 1, 3),
  ('Diego Barreda', 'Diego', 'Le agradecería que tuviera cuidado con las preguntas que hace y a quién se las hace. Marlecha es historia cerrada.', 1, 4),
  ('Andrés Salas', 'Clara', 'Andrés Salas, ¿verdad? He encontrado su nombre en unas notas de Tomás Elorza de 2005. ¿Qué pasó con la fotografía 17?', 9, 1),
  ('Andrés Salas', 'Andrés', '¿Quién le ha dado mi nombre?', 9, 2),
  ('Andrés Salas', 'Clara', 'Nadie. Lo encontré yo. Lleva veinte años sin salir a la luz y creo que usted lo sabe.', 9, 3),
  ('Andrés Salas', 'Andrés', 'No me crea. Compruébelo todo usted misma, con sus propias fuentes. Yo solo le voy a dar piezas, nunca la respuesta entera.', 9, 4),
  ('Andrés Salas', 'Andrés', 'He encontrado algo que Tomás dejó atrás.', 9, 5),
  ('Andrés Salas', 'Clara', '¿Qué es?', 9, 6),
  ('Andrés Salas', 'Andrés', 'No puedo explicárselo por aquí. Quedamos en persona.', 9, 7);

-- Correos: 4 contactos, 4 mensajes
INSERT INTO emails (sender, subject, body, min_chapter, display_order)
VALUES
  ('Marina Ortiz', 'Lo de esta noche', 'De: Marina Ortiz
Para: Clara Vega
Asunto: Lo de esta noche
Fecha: 3 de junio de 2025
Cuerpo:

Clara, ¿vienes luego a la despedida de Fermín? Lleva 31 años en el Faro y al final le vamos
a hacer algo en el bar de siempre, nada del otro mundo. Dile que sí aunque sea media hora,
que si no se va a poner triste y luego nos toca aguantarlo un mes.

Por cierto, ¿al final entregaste lo del puerto? Ibáñez ha preguntado dos veces esta mañana.

M.', 1, 1),
  ('Recursos Humanos — El Faro de San Adrián', 'Recordatorio: revisión de días de vacaciones pendientes', 'De: Recursos Humanos — El Faro de San Adrián
Para: Clara Vega
Asunto: Recordatorio: revisión de días de vacaciones pendientes
Fecha: 5 de junio de 2025
Cuerpo:

Estimada Clara Vega:

Le recordamos que, según nuestros registros, tiene 6 días de vacaciones del año en curso
todavía sin planificar. Le rogamos que lo comunique al departamento antes de fin de mes
para evitar la pérdida de los mismos según el convenio vigente.

Un saludo,
Recursos Humanos', 1, 2),
  ('Marina Ortiz', '¿Todo bien?', 'De: Marina Ortiz
Para: Clara Vega
Asunto: ¿Todo bien?
Fecha: 26 de junio de 2025
Cuerpo:

Oye, te noto rara últimamente. Llevas dos semanas comiendo sola en la mesa del rincón con
el portátil y contestando por monosílabos. Si es por lo del puerto, tranquila, a Ibáñez se
le olvida todo en cuanto sale algo nuevo.

Si es otra cosa y no quieres hablar de ello, vale, pero al menos dime que estás bien.

M.', 2, 1),
  ('Registro Mercantil de Asturias', 'Confirmación de solicitud — Expte. 2025-4471', 'De: Registro Mercantil de Asturias
Para: Clara Vega
Asunto: Confirmación de solicitud — Expte. 2025-4471
Fecha: 27 de junio de 2025
Cuerpo:

Estimada Sra. Vega,

Confirmamos la recepción de su solicitud de información registral (Expte. 2025-4471),
relativa al reportaje sobre concesiones del puerto pesquero que nos indica en su instancia.
El plazo estimado de respuesta es de 5 a 7 días hábiles.

Atentamente,
Registro Mercantil de Asturias', 2, 2);
