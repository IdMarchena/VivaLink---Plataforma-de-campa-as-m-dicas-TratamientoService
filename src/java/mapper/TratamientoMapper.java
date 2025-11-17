package mapper;

import modelo.EntidadDeSalud;
import modelo.Tratamiento;
import modelo.Usuario;
import dto.EntidadDeSaludDto;
import dto.TratamientoDto;
import dto.UsuarioDto;
import java.util.List;
import java.util.stream.Collectors;

public class TratamientoMapper {

    // Mapper para convertir de Usuario a UsuarioDto - CON MANEJO DE NULL
    public static UsuarioDto usuarioToDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioDto(
            usuario.getId(),
            usuario.getTelefono(),
            usuario.getFechaNacimiento(),
            usuario.getSexo(),
            usuario.getDireccion()
        );
    }

    // Mapper para convertir de UsuarioDto a Usuario (Entity) - CON MANEJO DE NULL
    public static Usuario dtoToUsuario(UsuarioDto usuarioDto) {
        if (usuarioDto == null) {
            return null;
        }
        return new Usuario(
            usuarioDto.id(),
            usuarioDto.telefono(),
            usuarioDto.fechaNacimiento(),
            usuarioDto.sexo(),
            usuarioDto.direccion()
        );
    }

    // Mapper para convertir de EntidadDeSalud a EntidadDeSaludDto - CON MANEJO DE NULL
    public static EntidadDeSaludDto entidadDeSaludToDto(EntidadDeSalud entidad) {
        if (entidad == null) {
            return null;
        }
        
        UsuarioDto usuarioDto = usuarioToDto(entidad.getUsuario());
        return new EntidadDeSaludDto(
            entidad.getId(),
            entidad.getTipo(),
            entidad.getNombre(),
            entidad.getDireccion(),
            entidad.getTelefono(),
            entidad.getIdentificador(),
            usuarioDto  // Puede ser null
        );
    }

    // Mapper para convertir de EntidadDeSaludDto a EntidadDeSalud (Entity) - CON MANEJO DE NULL
    public static EntidadDeSalud dtoToEntidadDeSalud(EntidadDeSaludDto entidadDto) {
        if (entidadDto == null) {
            return null;
        }
        
        Usuario usuario = dtoToUsuario(entidadDto.usuario());
        return new EntidadDeSalud(
            entidadDto.id(),
            entidadDto.tipo(),
            entidadDto.nombre(),
            entidadDto.direccion(),
            entidadDto.telefono(),
            entidadDto.identificador(),
            usuario  // Puede ser null
        );
    }

    // Mapper para convertir de Tratamiento a TratamientoDto - CON MANEJO DE NULL
    public static TratamientoDto tratamientoToDto(Tratamiento tratamiento) {
        if (tratamiento == null) {
            return null;
        }
        
        EntidadDeSaludDto entidadDeSaludDto = entidadDeSaludToDto(tratamiento.getEntidadDeSalud());
        UsuarioDto usuarioDto = usuarioToDto(tratamiento.getUsuario());
        
        return new TratamientoDto(
            tratamiento.getIdTratamiento(),
            tratamiento.getDescripcion(),
            tratamiento.getFechaInicio(),
            tratamiento.getFechaFin(),
            tratamiento.getEstado(),
            entidadDeSaludDto,  // Puede ser null
            usuarioDto,         // Puede ser null
            tratamiento.getComentarios()
        );
    }

    // Mapper para convertir de TratamientoDto a Tratamiento (Entity) - CON MANEJO DE NULL
    public static Tratamiento dtoToTratamiento(TratamientoDto dto) {
        if (dto == null) {
            return null;
        }
        
        // Convertir la EntidadDeSalud desde su DTO (puede ser null)
        EntidadDeSalud entidadDeSalud = dtoToEntidadDeSalud(dto.entidadDeSalud());

        // Convertir el Usuario desde su DTO (puede ser null)
        Usuario usuario = dtoToUsuario(dto.usuario());

        return new Tratamiento(
            dto.idTratamiento(),
            dto.descripcion(),
            dto.fechaInicio(),
            dto.fechaFin(),
            dto.estado(),
            entidadDeSalud,  // Puede ser null
            usuario,         // Puede ser null
            dto.comentarios()
        );
    }

    // Mapper para convertir de una lista de TratamientoDto a una lista de Tratamiento
    public static List<Tratamiento> dtoListToEntityList(List<TratamientoDto> dtoList) {
        if (dtoList == null) {
            return List.of();
        }
        return dtoList.stream()
            .map(TratamientoMapper::dtoToTratamiento)
            .collect(Collectors.toList());
    }

    // Mapper para convertir de una lista de Tratamiento a una lista de TratamientoDto
    public static List<TratamientoDto> entityListToDtoList(List<Tratamiento> entityList) {
        if (entityList == null) {
            return List.of();
        }
        return entityList.stream()
            .map(TratamientoMapper::tratamientoToDto)
            .collect(Collectors.toList());
    }
}