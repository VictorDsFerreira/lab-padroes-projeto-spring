# Explorando Padrões de Projeto com Spring Boot

Este projeto demonstra padrões de projeto usando Spring Boot, com foco em `Singleton`, `Strategy/Repository` e `Facade`. A API expõe operações CRUD de `Cliente`, integra com a API ViaCEP para resolver endereços e usa H2 em memória.

## Tecnologias
- Java 11
- Spring Boot 2.5.x
- Spring Web, Spring Data JPA, OpenFeign
- H2 Database
- Bean Validation (JSR 380)
- springdoc-openapi (Swagger UI)

## Como executar
1. Requisitos: JDK 11+ e Maven.
2. Build e run:
   - Linux/Mac: `./mvnw spring-boot:run`
   - Windows: `mvnw.cmd spring-boot:run`
3. A aplicação sobe em `http://localhost:8080`.

## Documentação da API
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Endpoints principais
- `GET /clientes` — lista todos os clientes
- `GET /clientes/{id}` — obtém um cliente por id
- `POST /clientes` — cria um cliente (201 Created)
- `PUT /clientes/{id}` — atualiza um cliente (200 OK)
- `DELETE /clientes/{id}` — remove um cliente (204 No Content)

### Exemplo de payload (POST/PUT)
```json
{
  "nome": "João Silva",
  "endereco": {
    "cep": "01001000"
  }
}
```

## Validação e erros
- Validação de entrada:
  - `Cliente.nome` obrigatório (`@NotBlank`)
  - `Cliente.endereco` obrigatório (`@NotNull`)
- Tratamento global de erros (`@RestControllerAdvice`):
  - 404 Not Found quando o recurso não existe
  - 400 Bad Request para erros de validação

Respostas de erro seguem um corpo simples com `timestamp`, `status`, `error` e `message`.

## Persistência e ViaCEP
- Endereços são buscados por CEP via ViaCEP e cacheados no banco H2.
- Relacionamento `Cliente` → `Endereco` é `@ManyToOne`.

## Como testar rapidamente (cURL)
```bash
# Criar cliente
curl -i -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome":"Maria","endereco":{"cep":"01001000"}}'

# Listar
curl -i http://localhost:8080/clientes

# Buscar por id
curl -i http://localhost:8080/clientes/1

# Atualizar
curl -i -X PUT http://localhost:8080/clientes/1 \
  -H "Content-Type: application/json" \
  -d '{"nome":"Maria Souza","endereco":{"cep":"01001000"}}'

# Remover
curl -i -X DELETE http://localhost:8080/clientes/1
```

## Notas e próximos passos
- Recomenda-se migrar para Java 17 e Spring Boot 3.x.
- Expor DTOs para entrada/saída e mapear com MapStruct.
- Adicionar paginação (`Pageable`) em `GET /clientes`.
