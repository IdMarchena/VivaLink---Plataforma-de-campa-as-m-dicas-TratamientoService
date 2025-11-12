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

    // Mapper para convertir de Usuario a UsuarioDto
    public static UsuarioDto usuarioToDto(Usuario usuario) {
        return new UsuarioDto(
            usuario.getId(),
            usuario.getTelefono(),
            usuario.getFechaNacimiento(),
            usuario.getSexo(),
            usuario.getDireccion()
        );
    }

    // Mapper para convertir de UsuarioDto a Usuario (Entity)
    public static Usuario dtoToUsuario(UsuarioDto usuarioDto) {
        return new Usuario(
            usuarioDto.id(),
            usuarioDto.telefono(),
            usuarioDto.fechaNacimiento(),
            usuarioDto.sexo(),
            usuarioDto.direccion()
        );
    }

    // Mapper para convertir de EntidadDeSalud a EntidadDeSaludDto
    public static EntidadDeSaludDto entidadDeSaludToDto(EntidadDeSalud entidad) {
        UsuarioDto usuarioDto = usuarioToDto(entidad.getUsuario());
        return new EntidadDeSaludDto(
            entidad.getId(),
            entidad.getTipo(),
            entidad.getNombre(),
            entidad.getDireccion(),
            entidad.getTelefono(),
            entidad.getIdentificador(),
            usuarioDto
        );
    }

    // Mapper para convertir de EntidadDeSaludDto a EntidadDeSalud (Entity)
    public static EntidadDeSalud dtoToEntidadDeSalud(EntidadDeSaludDto entidadDto) {
        Usuario usuario = dtoToUsuario(entidadDto.usuario());
        return new EntidadDeSalud(
            entidadDto.id(),
            entidadDto.tipo(),
            entidadDto.nombre(),
            entidadDto.direccion(),
            entidadDto.telefono(),
            entidadDto.identificador(),
            usuario
        );
    }

    // Mapper para convertir de Tratamiento a TratamientoDto
    public static TratamientoDto tratamientoToDto(Tratamiento tratamiento) {
        EntidadDeSaludDto entidadDeSaludDto = entidadDeSaludToDto(tratamiento.getEntidadDeSalud());
        UsuarioDto usuarioDto = usuarioToDto(tratamiento.getUsuario());
        return new TratamientoDto(
            tratamiento.getIdTratamiento(),
            tratamiento.getDescripcion(),
            tratamiento.getFechaInicio(),
            tratamiento.getFechaFin(),
            tratamiento.getEstado(),
            entidadDeSaludDto,
            usuarioDto,
            tratamiento.getComentarios()
        );
    }

    // Mapper para convertir de TratamientoDto a Tratamiento (Entity)
    public static Tratamiento dtoToTratamiento(TratamientoDto dto) {
        // Convertir la EntidadDeSalud desde su DTO
        EntidadDeSalud entidadDeSalud = dtoToEntidadDeSalud(dto.entidadDeSalud());

        // Convertir el Usuario desde su DTO
        Usuario usuario = dtoToUsuario(dto.usuario());

        return new Tratamiento(
            dto.idTratamiento(),
            dto.descripcion(),
            dto.fechaInicio(),
            dto.fechaFin(),
            dto.estado(),
            entidadDeSalud,
            usuario,
            dto.comentarios()
        );
    }

    // Mapper para convertir de una lista de TratamientoDto a una lista de Tratamiento
    public static List<Tratamiento> dtoListToEntityList(List<TratamientoDto> dtoList) {
        return dtoList.stream()
            .map(TratamientoMapper::dtoToTratamiento)
            .collect(Collectors.toList());
    }

    // Mapper para convertir de una lista de Tratamiento a una lista de TratamientoDto
    public static List<TratamientoDto> entityListToDtoList(List<Tratamiento> entityList) {
        return entityList.stream()
            .map(TratamientoMapper::tratamientoToDto)
            .collect(Collectors.toList());
    }
}
