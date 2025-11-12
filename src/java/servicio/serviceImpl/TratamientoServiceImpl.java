package servicio.serviceImpl;

import dto.EntidadDeSaludDto;
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
    private final EntidadDeSaludConsumer entidadDeSaludConsumer;

    // Constructor que recibe el tipo de base de datos
    public TratamientoServiceImpl(String tipoDb) throws SQLException {
        this.tratamientoRepository = new TratamientoRepository(tipoDb);
        this.entidadDeSaludConsumer = new EntidadDeSaludConsumer(); // Instanciamos el consumidor externo
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
    public void guardar(EntidadDeSaludDto entidadDeSaludDto, String tipo) {
        // Verificamos que la entidad de salud exista
        if (!entidadDeSaludConsumer.entidadExiste(entidadDeSaludDto.identificador())) {
            throw new IllegalArgumentException("La entidad de salud no existe.");
        }

        // Convertimos el DTO de EntidadDeSalud a la entidad
        // Convertimos el DTO de EntidadDeSalud a la entidad correspondiente
        var entidadDeSalud = TratamientoMapper.dtoToEntidadDeSalud(entidadDeSaludDto);

        // Guardamos el tratamiento en la base de datos
        tratamientoRepository.guardarTratamiento(entidadDeSalud, tipo);
    }

    @Override
    public List<TratamientoDto> buscarTratamientoPorNombre(String tipo) {
        return TratamientoMapper.entityListToDtoList(tratamientoRepository.buscarTratamientosPorNombre(tipo));
    }

    @Override
    public List<TratamientoDto> listarTodosLosTratamientos() {
        return TratamientoMapper.entityListToDtoList(tratamientoRepository.listarTodosLosTratamientos());
    }

    @Override
    public void actualizarTratamiento(int id, TratamientoDto tratamientoDto, String tipo) {
        // Convertimos el DTO de Tratamiento a la entidad correspondiente
        var tratamiento = TratamientoMapper.dtoToTratamiento(tratamientoDto);

        // Actualizamos el tratamiento en la base de datos
        tratamientoRepository.actualizarTratamiento(id, tratamiento, tipo);
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
