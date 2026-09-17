# Sprint 1 Evidence

## Sprint Goal

Configurar el workflow mediante GitHub Actions para el proyecto de Java, logrando la compilación automatizada y la inspección de código con SonarCloud en cada Pull Request a main.

## Incremento demostrable

Se logró implementar el workflow, el repositorio tiene un archivo YAML que se dispara al crear Pull Request hacia la rama main. Este descarga el código, configura el entorno, compila el proyecto y envía el análisis de código a SonarCloud, conectando ambas plataformas y activando el Quality Gate.

## Evidencia aplicable

- Archivo de configuración calidad.yml
- Ejecución exitosa en GitHub Actions
- Enlace al Pull Request: https://github.com/vzlaisa/orderflow-253088-244825-253301/pull/2
- Quality Gate de SonarCloud

## Diagnóstico, decisión y trade-off

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
  Decisión: Se agregó el parámetro -Dsonar.organization directamente al comando de Maven en el YAML para autorizar correctamente la subida del reporte.

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

## Uso de IA

Se utilizó IA para facilitar el debugging.
