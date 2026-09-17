# OrderFlow — Student Starter

OrderFlow funciona localmente, pero deliberadamente NO contiene el delivery system del curso.

## ¿De qué trata el sistema?

OrderFlow simula el procesamiento de pedidos de una empresa. Su API permite registrar pedidos asociados con un cliente, consultar los pedidos existentes y verificar el estado de salud del servicio. También incluye un componente de notificaciones que, más adelante, se preparará para ejecutarse como una función AWS Lambda.

La funcionalidad de negocio inicial es deliberadamente pequeña porque el propósito del proyecto no es construir una tienda completa. Durante el semestre, el equipo transformará la manera en que OrderFlow se integra, prueba, empaqueta, entrega, despliega, aprovisiona y observa mediante prácticas DevOps reproducibles.

## Arquitectura
 
El proyecto cuenta con dos módulos independientes:
 
| Módulo | Tecnología | Responsabilidad |
|---|---|---|
| `orders-api` | Spring Boot 3 (Java 21) | API REST para crear y consultar pedidos, con almacenamiento en memoria y health endpoint vía Actuator |
| `notifications-lambda` | Java plano (sin framework aún) | Genera mensajes de confirmación de pedido; preparado para evolucionar a una función AWS Lambda |

## Requisitos

- Java 21
- Maven 3.9+
- Git

Más adelante: Docker, AWS CLI, Terraform, Minikube, kubectl y Kompose.

## Bootstrap del repositorio del equipo

Cada equipo crea en la Sesión 1 su propio repositorio GitHub, por ejemplo `orderflow-equipo-03`. Ese repositorio será la **source of truth** durante todo el semestre.

Importen el starter y creen el baseline:

```bash
git init
git add .
git commit -m "chore: import OrderFlow baseline"
git branch -M main
git remote add origin <URL>
git push -u origin main
```

Otro integrante verifica la reproducibilidad desde un fresh clone:

```bash
git clone <URL>
cd <repo>
git log --oneline -1
mvn clean test
mvn package
```

Branching formal inicia en Semana 2. Pull Requests, Code Review y quality gates inician en Semana 3.

## Baseline

```bash
mvn clean test
mvn package
mvn -pl orders-api spring-boot:run
```

Prueba:

```bash
curl http://localhost:8080/actuator/health
Invoke-RestMethod -Uri "http://localhost:8080/api/orders" -Method Post -ContentType "application/json" -Body '{"customerId":"team-demo","total":150.00}'
curl http://localhost:8080/api/orders
```

## API de `orders-api`
 
Base path: `/api/orders`
 
| Método | Ruta | Descripción | Body |
|---|---|---|---|
| `POST` | `/api/orders` | Crea un pedido | `{"customerId": string, "total": number}` |
| `GET` | `/api/orders` | Lista todos los pedidos | — |
| `GET` | `/api/orders/{id}` | Consulta un pedido por id (404 si no existe) | — |
| `GET` | `/actuator/health` | Estado de salud del servicio | — |
 
Reglas de validación al crear un pedido:
- `customerId` es requerido (no nulo ni vacío).
- `total` debe ser un número positivo.
Cada pedido creado recibe un `id` autoincremental (inicia en 1001) y un estado inicial `CREATED`. Los estados posibles son `CREATED`, `CONFIRMED` y `CANCELLED`. El almacenamiento es en memoria, por lo que los datos se pierden al reiniciar el servicio.
 
## Módulo `notifications-lambda`
 
Contiene la lógica de generación de mensajes de confirmación (`NotificationService`), aún sin integración con AWS. `NotificationMessage` es el registro con `orderId`, `customerId` y el texto del mensaje. Este módulo se preparará más adelante en el curso para ejecutarse como una función AWS Lambda.
 
## Pruebas
 
Cada módulo cuenta con pruebas unitarias con JUnit 5:
 
```bash
mvn clean test
```
 
- `OrderServiceTest`: valida la creación correcta de pedidos y el rechazo de totales negativos.
- `NotificationServiceTest`: valida la generación del mensaje de confirmación.

## Análisis de calidad (SonarCloud)
 
El workflow `.github/workflows/calidad.yml` corre en cada push y pull request contra `main`:
 
1. Checkout del repositorio.
2. Configuración de Java.
3. `mvn verify` (build y pruebas) seguido del plugin de Sonar para Maven, que envía el análisis a **SonarCloud**.

### Requisitos para que el análisis corra
 
- Una organización creada en [SonarCloud](https://sonarcloud.io).
- Un proyecto en esa organización vinculado a este repositorio.
- Los secrets configurados en **Settings → Secrets and variables → Actions** del repositorio de GitHub (no se exponen credenciales):
  
| Secret | Para qué se usa |
|---|---|
| `SONAR_TOKEN` | Token de autenticación contra SonarCloud (permiso de análisis sobre el proyecto) |
| `SONAR_PROJECT_KEY` | Identifica el proyecto dentro de SonarCloud |
| `SONAR_ORGANIZATION` | Organization key de SonarCloud (obligatoria en SonarCloud, a diferencia de una instancia self-hosted) |
| `SONAR_HOST_URL` | URL del servidor Sonar (`https://sonarcloud.io` para SonarCloud) |
| `GITHUB_TOKEN` | Provisto automáticamente por GitHub Actions, no se configura manualmente |

### Ejecutar el análisis en local
 
Requiere tener `SONAR_TOKEN`, `SONAR_PROJECT_KEY`, `SONAR_ORGANIZATION` y `SONAR_HOST_URL` como variables de entorno o pasarlos como propiedades `-D`:
 
```bash
mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
  -Dsonar.projectKey=$SONAR_PROJECT_KEY \
  -Dsonar.organization=$SONAR_ORGANIZATION \
  -Dsonar.host.url=$SONAR_HOST_URL \
  -Dsonar.token=$SONAR_TOKEN
```
 
El resultado queda visible en el dashboard del proyecto en SonarCloud.

## Evidencia acumulativa

No sobrescriban evidencias anteriores. Cada Sprint conserva su propio archivo:

```text
docs/evidence/
├── sprint-00.md
├── sprint-01.md
├── sprint-02.md
├── sprint-03.md
├── sprint-04.md
├── sprint-05.md
├── sprint-06.md
└── sprint-07.md
```

Usen PR, pipeline, deployment o infraestructura sólo cuando ya correspondan al Sprint. Antes de eso registren: `N/A — todavía no corresponde a este Sprint.`

## Regla del semestre

No implementen por adelantado `.github/workflows`, `delivery`, `infra`, `k8s` u `observability`. Esas carpetas se desarrollan progresivamente como evidencia de aprendizaje.
