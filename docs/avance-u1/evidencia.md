# Sprint 1 / Avance unidad 1 Evidence

## Sprint goal

Configurar el workflow mediante GitHub Actions para el proyecto de Java, logrando la compilación automatizada y la inspección de código con SonarCloud en cada Pull Request a main.

## Incremento demostrable

Se logró implementar el workflow, el repositorio tiene un archivo YAML que se dispara al crear Pull Request hacia la rama main. Este descarga el código, configura el entorno, compila el proyecto y envía el análisis de código a SonarCloud, conectando ambas plataformas y activando el Quality Gate.

## Identificación
Equipo: #3
Integrantes: Ximena Rosales Panduro (253088), Isabel Valenzuela Rocha (253301), Camila Zubía Higuera (244825)
Proyecto: OrderFlow
Repositorio: https://github.com/vzlaisa/orderflow-253088-244825-253301
Commit de entrega: d3f0c9d

## Flujo
- El workflow  se ejecuta en cada pull request hacia la rama main.
- Corre en ubuntu-latest.
- Steps del workflow: checkout del repositorio, configuración de Java 21 (Temurin), y el paso "Build and Analyze" que corre mvn verify (compila y prueba el proyecto) seguido del plugin de Sonar para Maven.
- Función de Sonar: recibe el reporte de análisis generado por el plugin de Maven, lo evalúa contra el Quality Gate configurado en SonarCloud, y ese resultado queda asociado al Pull Request.

## Predicción
- Se esperaba que compilara y pasara las pruebas sin problema, ya que anteriormente había funcionado manualmente en local.
- No se tenía certeza de que el workflow funcionara, ya que era la primera vez que el equipo configuraba uno.

## Observación
- La carpeta .github estaba anidada en una subcarpeta, por lo que GitHub no detectaba el workflow y este nunca se disparaba.
- El log del run mostró un error de versión de Java, la acción de SonarQube en Docker usaba Java 11, mientras el proyecto está compilado en Java 21.
- Una vez resuelto lo anterior, el step "Build and Analyze" falló con el mensaje: You must define the following mandatory properties for 'Project OrderFlow': sonar.organization.

## Correcciones (Diagnóstico, decisión y trade-off)

- El workflow no se activaba:
  Diagnóstico: La carpeta .github estaba anidada por lo que GitHub no la detectaba.
  Decisión: Se movieron los archivos a la raíz.
  Trade-off: Requirió reescribir el historial de ubicaciones en Git, pero garantizó que tanto GitHub Actions como Maven funcionaran correctamente.

- Error de versión de Java (55.0 vs 61.0):
  Diagnóstico: La acción de SonarQube en Docker utilizaba Java 11, mientras que el proyecto está compilado en Java 21.
  Decisión: En lugar de usar la acción genérica, se usó el plugin de Maven.
  Trade-off: Acopla ligeramente el análisis a Maven, pero elimina la dependencia de contenedores de Docker de terceros.

- Falta de propiedad sonar.organization:
  Diagnóstico: El análisis fallaba al conectarse con SonarCloud porque requería el identificador de la organización.
  Decisión: Se agregó el parámetro Dsonar.organization=${{ secrets.SONAR_ORGANIZATION }} directamente al comando de Maven en el YAML para autorizar correctamente la subida del reporte.

## Comparación antes/después

Antes de estas correcciones el workflow no se disparaba, luego se disparaba pero fallaba el build por la versión de Java, y finalmente fallaba solo el envío a SonarCloud por falta de sonar.organization. Después de los tres cambios, el step "Build and Analyze" se completa correctamente y el análisis llega a SonarCloud.

## Decisión

- La integración se bloquea si el Quality Gate no pasa. Se bloqueaba porque no cumplía con el coverage >= 80%.
- Un Quality Gate en verde confirma que el código cumple las condiciones automatizadas configuradas, pero aún requiere validación manual de lógica y código en el PR.

## Evidencia aplicable

- Archivo de configuración: .github/workflows/calidad.yml
- Hash del commit README: 6c8e10e
- Enlaces a los runs: https://github.com/vzlaisa/orderflow-253088-244825-253301/actions/runs/35141783251 (primer fallido) y https://github.com/vzlaisa/orderflow-253088-244825-253301/actions/runs/35144551964 (exitoso tras correcciones).
- Enlace al Pull Request: https://github.com/vzlaisa/orderflow-253088-244825-253301/pull/2
- Quality Gate de SonarCloud (evidencia en capturas)

## Contribuciones del equipo

- Isabel: Realizó el workflow, configuró SonarCloud al proyecto y escribió el README.
- Camila: Hizó el Pull Request y modificaciones para que no tirara error.
- Ximena: Identificó como marcar un error en el Quality Gate y lo corrigió.

## Mini Definition of Done

- Se redactó el README
- El archivo calidad.yml está en la ruta correcta.
- El workflow se dispara automáticamente al actualizar un Pull Request hacia main.
- El proyecto compila correctamente usando Maven y Java 21 en la nube.
- SonarCloud recibe el reporte de análisis.
- El Pull Request es evaluado por el Quality Gate de SonarCloud.
- El error en Quality Gate fue corregido.

## Retro: Keep / Change / Next experiment

Keep: Mantener el uso de Maven para la compilación del análisis.
Change: evitar usar subcarpetas, utilizar comandos actualizados.
Next experiment: Configurar el plugin de JaCoCo en el pom.xml para habilitar el reporte de cobertura de pruebas (Coverage), y refactorizar los code smells de Java para lograr que el Quality Gate pase a estado aprobado (Verde).

## Limitaciones y uso de IA

- Se debe usar el Sonar Way para el Quality Gate, ya que SonarCloud limita cambiarlo o crear uno propio.
- Se utilizó IA para facilitar el debugging del workflow. Las correcciones propuestas fueron revisadas, aplicadas y verificadas por el equipo mediante su ejecución.