# Memoria del Proyecto

Este archivo es la memoria persistente de este proyecto. Aquí se registran errores conocidos, decisiones técnicas y advertencias para evitar repetir problemas en el futuro. Ante cualquier error nuevo relevante, consignarlo en este archivo.

## Errores conocidos

### `ddl-auto: update` no actualiza CHECK constraints de enums

Al agregar un valor a `MovementReason` (o cualquier enum mapeado con `@Enumerated(EnumType.STRING)`), Hibernate **no modifica** el CHECK constraint existente en la DB. La inserción falla con `violates check constraint`. Fix manual obligatorio:

```sql
ALTER TABLE <tabla> DROP CONSTRAINT <tabla>_movement_reason_check;
ALTER TABLE <tabla> ADD CONSTRAINT <tabla>_movement_reason_check
  CHECK (movement_reason::text = ANY (ARRAY['VALOR1', 'VALOR2', ...]::text[]));
```

Tablas afectadas actualmente: `raw_material_movement`, `product_inventory_movement`.
