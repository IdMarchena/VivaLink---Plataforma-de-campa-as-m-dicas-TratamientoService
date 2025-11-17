package servicio.serviceImpl;
import dto.TratamientoDto;
import external.EntidadDeSaludConsumer;
import mapper.TratamientoMapper;
import repositorio.TratamientoRepository;
import servicio.service.TratamientoService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TratamientoServiceImpl implements TratamientoService {

    private final TratamientoRepository tratamientoRepository;

    // Constructor que recibe el tipo de base de datos
    public TratamientoServiceImpl(String tipoDb) throws SQLException {
        this.tratamientoRepository = new TratamientoRepository();
    }

    @Override
    public TratamientoDto buscarTratamientoPorId(int id) {
        return TratamientoMapper.tratamientoToDto(tratamientoRepository.buscarTratamientoPorId(id));
    }

    @Override
    public boolean verificarSiElTratamientoExiste(int id) {
        return tratamientoRepository.verificarSiElTratamientoExiste(id);
    }

    @Override
    public void guardar(TratamientoDto tratamiento) {
        var Tratamietno = TratamientoMapper.dtoToTratamiento(tratamiento);
        tratamientoRepository.guardarTratamiento(Tratamietno);
    }

    @Override
    public List<TratamientoDto> buscarTratamientoPorDescripcion(String descripcion) {
        return TratamientoMapper.entityListToDtoList(tratamientoRepository.buscarTratamientosPorNombre(descripcion));
    }

    @Override
    public List<TratamientoDto> listarTodosLosTratamientos() {
        return TratamientoMapper.entityListToDtoList(tratamientoRepository.listarTodosLosTratamientos());
    }

    @Override
    public void actualizarTratamiento(int id, TratamientoDto tratamientoDto) {
        // Convertimos el DTO de Tratamiento a la entidad correspondiente
        var tratamiento = TratamientoMapper.dtoToTratamiento(tratamientoDto);

        // Actualizamos el tratamiento en la base de datos
        tratamientoRepository.actualizarTratamiento(id, tratamiento);
    }

    @Override
    public void eliminarTratamiento(int id) {
        tratamientoRepository.eliminarTratamiento(id);
    }

    @Override
    public List<TratamientoDto> buscarTratamientosPorFecha(LocalDate fecha) {
        return TratamientoMapper.entityListToDtoList(tratamientoRepository.buscarTratamientosPorFecha(fecha));
    }

    @Override
    public List<TratamientoDto> buscarTratamientosPorEstado(String estado) {
        return TratamientoMapper.entityListToDtoList(tratamientoRepository.buscarTratamientosPorEstado(estado));
    }
}
