# Makefile para MercadoLibre Local Environment
# Facilita la ejecución de comandos comunes

.PHONY: help setup dev start stop clean test logs status health

# Colores para output
BLUE := \033[0;34m
GREEN := \033[0;32m
YELLOW := \033[1;33m
RED := \033[0;31m
NC := \033[0m # No Color

## Mostrar ayuda
help:
	@echo "$(BLUE)🚀 MercadoLibre - Comandos Disponibles$(NC)"
	@echo ""
	@echo "$(GREEN)📋 COMANDOS PRINCIPALES:$(NC)"
	@echo "  $(YELLOW)make setup$(NC)     - Configuración completa con Docker"
	@echo "  $(YELLOW)make dev$(NC)       - Configuración para desarrollo"
	@echo "  $(YELLOW)make start$(NC)     - Iniciar aplicación completa"
	@echo "  $(YELLOW)make stop$(NC)      - Detener todos los servicios"
	@echo "  $(YELLOW)make restart$(NC)   - Reiniciar servicios"
	@echo ""
	@echo "$(GREEN)🔧 DESARROLLO:$(NC)"
	@echo "  $(YELLOW)make dev-backend$(NC)  - Iniciar solo backend en desarrollo"
	@echo "  $(YELLOW)make dev-frontend$(NC) - Iniciar solo frontend en desarrollo"
	@echo "  $(YELLOW)make test$(NC)         - Ejecutar todos los tests"
	@echo "  $(YELLOW)make build$(NC)        - Construir proyectos"
	@echo ""
	@echo "$(GREEN)📊 MONITOREO:$(NC)"
	@echo "  $(YELLOW)make logs$(NC)      - Ver logs de todos los servicios"
	@echo "  $(YELLOW)make status$(NC)    - Ver estado de servicios"
	@echo "  $(YELLOW)make health$(NC)    - Verificar health de servicios"
	@echo ""
	@echo "$(GREEN)🧹 LIMPIEZA:$(NC)"
	@echo "  $(YELLOW)make clean$(NC)     - Limpiar contenedores y volúmenes"
	@echo "  $(YELLOW)make clean-all$(NC) - Limpieza completa del sistema"
	@echo ""

## Configuración completa con Docker
setup:
	@echo "$(BLUE)🚀 Configurando entorno completo...$(NC)"
	@./setup-local.sh

## Configuración para desarrollo
dev:
	@echo "$(BLUE)🔧 Configurando entorno de desarrollo...$(NC)"
	@./setup-dev.sh

## Iniciar aplicación completa
start:
	@echo "$(BLUE)▶️  Iniciando aplicación...$(NC)"
	@./start.sh

## Detener servicios
stop:
	@echo "$(RED)⏹️  Deteniendo servicios...$(NC)"
	@./stop.sh

## Reiniciar servicios
restart: stop start

## Iniciar backend en desarrollo
dev-backend:
	@echo "$(BLUE)🔧 Iniciando backend en modo desarrollo...$(NC)"
	@cd ml-core-platform && mvn spring-boot:run -Dspring-boot.run.profiles=local

## Iniciar frontend en desarrollo
dev-frontend:
	@echo "$(BLUE)⚡ Iniciando frontend en modo desarrollo...$(NC)"
	@cd ml_item_product && npm run dev

## Construir proyectos
build:
	@echo "$(BLUE)🔨 Construyendo proyectos...$(NC)"
	@docker-compose build
	@echo "$(GREEN)✅ Construcción completada$(NC)"

## Ejecutar tests
test:
	@echo "$(BLUE)🧪 Ejecutando tests...$(NC)"
	@echo "$(YELLOW)Tests del backend:$(NC)"
	@cd ml-core-platform && mvn test
	@echo "$(YELLOW)Tests del frontend:$(NC)"
	@cd ml_item_product && npm test run

## Ver logs de todos los servicios
logs:
	@echo "$(BLUE)📋 Mostrando logs...$(NC)"
	@docker-compose logs -f

## Ver logs del frontend
logs-frontend:
	@docker-compose logs -f ml-item-product

## Ver logs del backend
logs-backend:
	@docker-compose logs -f ml-core-platform

## Ver logs de la base de datos
logs-db:
	@docker-compose logs -f ml-database

## Ver estado de servicios
status:
	@echo "$(BLUE)📊 Estado de servicios:$(NC)"
	@docker-compose ps

## Verificar health de servicios
health:
	@echo "$(BLUE)❤️  Verificando salud de servicios...$(NC)"
	@echo "$(YELLOW)Backend Health:$(NC)"
	@curl -s http://localhost:8080/actuator/health | jq . 2>/dev/null || echo "Backend no disponible"
	@echo "$(YELLOW)Frontend:$(NC)"
	@curl -s -o /dev/null -w "%{http_code}" http://localhost:3000 | grep -q "200" && echo "Frontend OK" || echo "Frontend no disponible"

## Limpiar contenedores y volúmenes
clean:
	@echo "$(RED)🧹 Limpiando contenedores y volúmenes...$(NC)"
	@docker-compose down -v
	@docker system prune -f

## Limpieza completa del sistema
clean-all:
	@echo "$(RED)🗑️  Limpieza completa del sistema...$(NC)"
	@docker-compose down -v --rmi all
	@docker system prune -a -f

## Conectar a la base de datos
db:
	@echo "$(BLUE)🗄️  Conectando a PostgreSQL...$(NC)"
	@docker-compose exec ml-database psql -U mluser -d mlcoreplatform

## Conectar a Redis
redis:
	@echo "$(BLUE)🔴 Conectando a Redis...$(NC)"
	@docker-compose exec ml-redis redis-cli

## Abrir URLs en el navegador (macOS)
open:
	@echo "$(BLUE)🌐 Abriendo URLs...$(NC)"
	@open http://localhost:3000
	@open http://localhost:8080/swagger-ui.html

## Mostrar información del proyecto
info:
	@echo "$(BLUE)📋 Información del Proyecto$(NC)"
	@echo ""
	@echo "$(GREEN)Frontend:$(NC) ml_item_product (React + Vite)"
	@echo "$(GREEN)Backend:$(NC)  ml-core-platform (Spring Boot)"
	@echo ""
	@echo "$(YELLOW)URLs:$(NC)"
	@echo "  Frontend: http://localhost:3000"
	@echo "  Backend:  http://localhost:8080"
	@echo "  Swagger:  http://localhost:8080/swagger-ui.html"
	@echo "  Health:   http://localhost:8080/actuator/health"

## Verificar que todo esté funcionando
check:
	@echo "$(BLUE)🔍 Verificando sistema completo...$(NC)"
	@make status
	@echo ""
	@make health

# Configuración por defecto
.DEFAULT_GOAL := help
