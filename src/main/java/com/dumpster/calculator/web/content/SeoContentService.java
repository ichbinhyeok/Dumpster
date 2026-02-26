package com.dumpster.calculator.web.content;

import com.dumpster.calculator.infra.persistence.MaterialFactorRepository;
import com.dumpster.calculator.web.viewmodel.MaterialPageViewModel;
import com.dumpster.calculator.web.viewmodel.ProjectPageViewModel;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SeoContentService {

    private final MaterialFactorRepository materialFactorRepository;
    private final Map<String, ProjectPageViewModel> projectPages = new LinkedHashMap<>();

    public SeoContentService(MaterialFactorRepository materialFactorRepository) {
        this.materialFactorRepository = materialFactorRepository;
        projectPages.put("roof_tearoff", new ProjectPageViewModel(
                "roof_tearoff",
                "Dumpster Size for Roof Tear-off",
                "roof_square",
                "asphalt_shingles",
                "Choosing 20yd by volume alone can trigger overweight fees.",
                "/dumpster/size/roof_tearoff"
        ));
        projectPages.put("kitchen_remodel", new ProjectPageViewModel(
                "kitchen_remodel",
                "Dumpster Size for Kitchen Remodel",
                "pickup_load",
                "mixed_cd",
                "Underestimating cabinet and countertop weight is common.",
                "/dumpster/size/kitchen_remodel"
        ));
        projectPages.put("concrete_removal", new ProjectPageViewModel(
                "concrete_removal",
                "Dumpster Strategy for Concrete Removal",
                "sqft_4in",
                "concrete",
                "Large bins are often not feasible for heavy concrete loads.",
                "/dumpster/size/concrete_removal"
        ));
        projectPages.put("yard_cleanup", new ProjectPageViewModel(
                "yard_cleanup",
                "Dumpster Size for Yard Cleanup",
                "pickup_load",
                "yard_waste",
                "Wet yard waste can spike weight quickly after rain.",
                "/dumpster/size/yard_cleanup"
        ));
    }

    public Optional<MaterialPageViewModel> materialPage(String materialId) {
        return materialFactorRepository.findById(materialId)
                .map(material -> {
                    double exampleVolume = 8.0d;
                    double typWeight = (exampleVolume * material.densityTyp()) / 2000.0d;
                    return new MaterialPageViewModel(
                            material.materialId(),
                            material.name() + " Dumpster Weight Guide",
                            material.densityLow(),
                            material.densityTyp(),
                            material.densityHigh(),
                            exampleVolume,
                            Math.round(typWeight * 100.0d) / 100.0d,
                            material.source()
                    );
                });
    }

    public Optional<ProjectPageViewModel> projectPage(String projectId) {
        return Optional.ofNullable(projectPages.get(projectId));
    }

    public Map<String, ProjectPageViewModel> projectPages() {
        return projectPages;
    }
}

