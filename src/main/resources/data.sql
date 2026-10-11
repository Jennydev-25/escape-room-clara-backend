-- Roles de usuario (registro/login)
INSERT INTO
  roles (name)
VALUES
  ('USER'),
  ('ADMIN');

-- Tipos de contacto (formulario de contacto)
INSERT INTO
  contact_types (name)
VALUES
  ('QUESTION'),
  ('BUG'),
  ('SUGGESTION');

-- Carpetas del escritorio, estructura de las pruebas
INSERT INTO
  folders (code, name, min_chapter_to_open)
VALUES
  ('leeme', 'Leeme', 1),
  ('tomas', 'Tomás', 2);

-- Puzzles + pistas (hint1/2/3): pendiente

-- Papelera (DiscardedFile): P1-P5
INSERT INTO
  discarded_files (name, type, body, min_chapter)
VALUES
  (
    'borrador_reportaje_puerto_v1.txt',
    'TEXT',
    '[BORRADOR — sin terminar]

El puerto pesquero de San Adrián lleva perdiendo licencias de amarre desde 2019, y este
año la cofradía ha pedido explicaciones al Ayuntamiento sin obtener respuesta clara.

Falta: cifra exacta de licencias perdidas (pedida al Registro, pendiente). Falta: reacción
del Ayuntamiento. Falta: título mejor que este, que es horrible.',
    1
  );

INSERT INTO
  discarded_files (name, type, body, min_chapter)
VALUES
  (
    'notas_ayuntamiento_ronda.txt',
    'TEXT',
    'Reunión 14:30 — concejal de Urbanismo
- Dice que el proyecto de la ronda norte "sigue en fase de estudio" (lleva así 3 años)
- Le pregunto por el presupuesto, no lo tiene a mano, dice que me lo manda por correo
  (todavía no ha llegado, llevo 2 semanas esperando)
- Pedir declaración también a la asociación de vecinos antes de publicar nada',
    1
  );

INSERT INTO
  discarded_files (name, type, body, min_chapter)
VALUES
  (
    'ideas_sueltas.txt',
    'TEXT',
    '¿Por qué nadie de Marlecha S.A. quiere hablar, veinte años después? Puede que no sea nada,
puede que solo sea que ya no queda nadie a quien le importe una empresa que cerró en 2006.

Llamar otra vez al Ayuntamiento por si conservan algún expediente del cierre — probablemente
no aporte nada nuevo, era una empresa privada, no debería haber mucho papeleo público.

Descartado por ahora: no tengo nada concreto, solo la sensación de que hay algo.',
    2
  );

INSERT INTO
  discarded_files (name, type, body, min_chapter)
VALUES
  (
    'contactos_pendientes.txt',
    'TEXT',
    'Gente con la que probar a hablar, sin garantía de que lleve a nada:
- Antiguo capataz de Marlecha (no localizado, puede que haya fallecido)
- Alguien de la gestoría que hacía las cuentas antes de 2010 (nombre no confirmado)
- Vecinos de la zona del polígono, por si alguien recuerda algo de esos años

Ninguno de estos ha respondido todavía. Puede que no lleven a nada.',
    3
  );

INSERT INTO
  discarded_files (name, type, body, min_chapter)
VALUES
  (
    'salgo_fatal.png',
    'IMG',
    '/papelera/salgo_fatal.png',
    1
  );

-- Chat (ChatMessage): 4 contactos, 26 mensajes
INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Redacción · El Faro',
    'Javier',
    'recordatorio: hoy es el cumple de Ibáñez, hay bizcocho en la sala de reuniones a las 12',
    1,
    1
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Redacción · El Faro',
    'Marina',
    '¿alguien tiene el cargador de USB-C? el mío ha muerto en combate',
    1,
    2
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Redacción · El Faro',
    'Javier',
    'yo tengo, está en mi mesa, coge el que quieras',
    1,
    3
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Redacción · El Faro',
    'Marina',
    'eres mi héroe',
    1,
    4
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Redacción · El Faro',
    'Fermín',
    'como oiga la palabra bizcocho una vez más antes de las 12 me da algo',
    1,
    5
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Redacción · El Faro',
    'Javier',
    'Fermín amargado como siempre jaja',
    1,
    6
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Redacción · El Faro',
    'Marina',
    'Clara pareces zombi esta semana eh',
    1,
    7
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Redacción · El Faro',
    'Javier',
    'es verdad, ayer casi te quedas dormida en la reunión de las 9',
    1,
    8
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Redacción · El Faro',
    'Clara',
    'gracias por la preocupación jajaja estoy bien, solo cansada',
    1,
    9
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Redacción · El Faro',
    'Marina',
    'duerme algo anda, que luego no hay café que te arregle',
    1,
    10
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Mamá',
    'Mamá',
    'Hija, ¿este finde vienes a comer o otra vez tienes lío?',
    1,
    1
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Mamá',
    'Clara',
    'Este finde no puedo mamá, tengo un cierre. El que viene seguro',
    1,
    2
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Mamá',
    'Mamá',
    'Llevas semanas rara por teléfono. ¿Va todo bien de verdad?',
    1,
    3
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Mamá',
    'Clara',
    'Sí mamá, tranquila, es solo trabajo. Te llamo este finde',
    1,
    4
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Mamá',
    'Mamá',
    'Clara no me has llamado el finde pasado ni el anterior. Contesta cuando puedas, aunque sea un mensaje',
    1,
    5
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Diego Barreda',
    'Clara',
    'Buenas tardes, Sr. Barreda. Soy Clara Vega, de El Faro de San Adrián. Estoy preparando un reportaje sobre el cierre de la antigua conservera Marlecha y me gustaría hacerle un par de preguntas.',
    1,
    1
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Diego Barreda',
    'Diego',
    'Fue hace veinte años. No creo que tenga mucho que aportar que no esté ya en la prensa de la época.',
    1,
    2
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Diego Barreda',
    'Clara',
    'Solo dos preguntas más: ¿por qué se descartó parte del testimonio de Elena Sáez? ¿Y qué relación tiene usted con Norte Atlántico Servicios?',
    1,
    3
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Diego Barreda',
    'Diego',
    'Le agradecería que tuviera cuidado con las preguntas que hace y a quién se las hace. Marlecha es historia cerrada.',
    1,
    4
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Andrés Salas',
    'Clara',
    'Andrés Salas, ¿verdad? He encontrado su nombre en unas notas de Tomás Elorza de 2005. ¿Qué pasó con la fotografía 17?',
    9,
    1
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Andrés Salas',
    'Andrés',
    '¿Quién le ha dado mi nombre?',
    9,
    2
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Andrés Salas',
    'Clara',
    'Nadie. Lo encontré yo. Lleva veinte años sin salir a la luz y creo que usted lo sabe.',
    9,
    3
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Andrés Salas',
    'Andrés',
    'No me crea. Compruébelo todo usted misma, con sus propias fuentes. Yo solo le voy a dar piezas, nunca la respuesta entera.',
    9,
    4
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Andrés Salas',
    'Andrés',
    'He encontrado algo que Tomás dejó atrás.',
    9,
    5
  );

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  ('Andrés Salas', 'Clara', '¿Qué es?', 9, 6);

INSERT INTO
  chat_messages (
    contact,
    character,
    content,
    min_chapter,
    display_order
  )
VALUES
  (
    'Andrés Salas',
    'Andrés',
    'No puedo explicárselo por aquí. Quedamos en persona.',
    9,
    7
  );

-- Correo (Email): C1-C5
INSERT INTO
  emails (sender, subject, body, min_chapter)
VALUES
  (
    'Marina Ortiz',
    'Lo de esta noche',
    'De: Marina Ortiz
Para: Clara Vega
Asunto: Lo de esta noche
Fecha: 3 de junio de 2025
Cuerpo:

Clara, ¿vienes luego a la despedida de Fermín? Lleva 31 años en el Faro y al final le vamos
a hacer algo en el bar de siempre, nada del otro mundo. Dile que sí aunque sea media hora,
que si no se va a poner triste y luego nos toca aguantarlo un mes.

Por cierto, ¿al final entregaste lo del puerto? Ibáñez ha preguntado dos veces esta mañana.

M.',
    1
  );

INSERT INTO
  emails (sender, subject, body, min_chapter)
VALUES
  (
    'Recursos Humanos — El Faro de San Adrián',
    'Recordatorio: revisión de días de vacaciones pendientes',
    'De: Recursos Humanos — El Faro de San Adrián
Para: Clara Vega
Asunto: Recordatorio: revisión de días de vacaciones pendientes
Fecha: 5 de junio de 2025
Cuerpo:

Estimada Clara Vega:

Le recordamos que, según nuestros registros, tiene 6 días de vacaciones del año en curso
todavía sin planificar. Le rogamos que lo comunique al departamento antes de fin de mes
para evitar la pérdida de los mismos según el convenio vigente.

Un saludo,
Recursos Humanos',
    1
  );

INSERT INTO
  emails (sender, subject, body, min_chapter)
VALUES
  (
    'Marina Ortiz',
    '¿Todo bien?',
    'De: Marina Ortiz
Para: Clara Vega
Asunto: ¿Todo bien?
Fecha: 26 de junio de 2025
Cuerpo:

Oye, te noto rara últimamente. Llevas dos semanas comiendo sola en la mesa del rincón con
el portátil y contestando por monosílabos. Si es por lo del puerto, tranquila, a Ibáñez se
le olvida todo en cuanto sale algo nuevo.

Si es otra cosa y no quieres hablar de ello, vale, pero al menos dime que estás bien.

M.',
    2
  );

INSERT INTO
  emails (sender, subject, body, min_chapter)
VALUES
  (
    'Registro Mercantil de Asturias',
    'Confirmación de solicitud — Expte. 2025-4471',
    'De: Registro Mercantil de Asturias
Para: Clara Vega
Asunto: Confirmación de solicitud — Expte. 2025-4471
Fecha: 27 de junio de 2025
Cuerpo:

Estimada Sra. Vega,

Confirmamos la recepción de su solicitud de información registral (Expte. 2025-4471),
relativa al reportaje sobre concesiones del puerto pesquero que nos indica en su instancia.
El plazo estimado de respuesta es de 5 a 7 días hábiles.

Atentamente,
Registro Mercantil de Asturias',
    2
  );
