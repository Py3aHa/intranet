package kz.bitlab.intranet.controllers;

import kz.bitlab.intranet.entities.courses.Chapters;
import kz.bitlab.intranet.entities.courses.Courses;
import kz.bitlab.intranet.entities.courses.Lesson;
import kz.bitlab.intranet.entities.user.Roles;
import kz.bitlab.intranet.entities.user.Users;
import kz.bitlab.intranet.repository.ChaptersRepository;
import kz.bitlab.intranet.repository.CoursesRepository;
import kz.bitlab.intranet.repository.LessonsRepository;
import kz.bitlab.intranet.repository.UserRepository;
import kz.bitlab.intranet.services.CoursesService;
import kz.bitlab.intranet.services.RolesService;
import kz.bitlab.intranet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(path = "/admin")
public class AdminController extends BaseController{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private ChaptersRepository chaptersRepository;

    @Autowired
    private LessonsRepository lessonsRepository;

    @GetMapping(path = "/users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String users(Model model, @RequestParam(name = "page", defaultValue = "1") int page){
        int count = userRepository.countAllByDeletedAtNull();
        int tabSize = (count+9)/10;
        if(page < 1){
            page = 1;
        }
        Pageable pageable = PageRequest.of(page-1,10);
        List<Users> users = userService.getAllUsers(pageable);
        model.addAttribute("tabSize", tabSize);
        model.addAttribute("user", getUserData());
        model.addAttribute("users", users);

        return "admin/users";
    }

    @GetMapping(path = "/userdetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String userDetails(Model model, @PathVariable(name = "id") Long id){

        model.addAttribute("user", getUserData());
        Users userDetail = userService.getOneById(id);
        model.addAttribute("userDetail", userDetail);
        return "admin/userdetail";

    }

    @PostMapping(path = "/changeUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String changeUser(@RequestParam(name = "id") Long id, @RequestParam(name = "email") String email,
                             @RequestParam(name = "lastName") String lastName, @RequestParam(name = "firstName") String firstName,
                             @RequestParam(name = "student", required = false) Long student,
                             @RequestParam(name = "teacher", required = false) Long teacher, @RequestParam(name = "admin", required = false) Long admin){
        Roles role = new Roles();
        Set<Roles> setRoles = new HashSet<>();
        if(student != null){
            role = rolesService.getOneById(student);
            setRoles.add(role);
        }
        if(teacher != null){
            role = rolesService.getOneById(teacher);
            setRoles.add(role);
        }
        if(admin != null){
            role = rolesService.getOneById(admin);
            setRoles.add(role);
        }
        Users user = userRepository.getOne(id);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUpdatedAt(new Date());
        user.setRoles(setRoles);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping(path = "/addNewUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addNewUserPage(){
        return "/admin/addNewUser";
    }

    @PostMapping(path = "/addUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addUser(Model model, @RequestParam(name = "email") String email, @RequestParam(name = "lastName") String lastName,
                          @RequestParam(name = "firstName") String firstName, @RequestParam(name = "student", required = false) Long student,
                          @RequestParam(name = "teacher", required = false) Long teacher, @RequestParam(name = "admin", required = false) Long admin,
                          @RequestParam(name = "password") String password, @RequestParam(name = "repass") String repass){
        String redirect = "redirect:/admin/addUser?error";
        if(userService.getUserByEmail(email) == null){
            if (password.length() > 5) {
                if(password.equals(repass)){
                    Roles role = new Roles();
                    Set<Roles> setRoles = new HashSet<>();
                    if(student != null){
                        role = rolesService.getOneById(student);
                        setRoles.add(role);
                    }
                    if(teacher != null){
                        role = rolesService.getOneById(teacher);
                        setRoles.add(role);
                    }
                    if(admin != null){
                        role = rolesService.getOneById(admin);
                        setRoles.add(role);
                    }
                    Users user = new Users(email, password, firstName, lastName, setRoles);
                    user.setCreatedAt(new Date());
                    userRepository.save(user);
                    redirect = "redirect:/admin/users";
                }
            }
        }

        return redirect;
    }

    @PostMapping(path = "/deleteUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@RequestParam(name = "id") Long id){
        userRepository.delete(userService.getOneById(id));
        return "redirect:/admin/users";
    }







                                        //Courses

    @GetMapping(path = "/courses/courses")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String courses(Model model, @RequestParam(name = "page", defaultValue = "1") int page){
        int count = userRepository.countAllByDeletedAtNull();
        int tabSize = (count+9)/10;
        if(page < 1){
            page = 1;
        }
        Pageable pageable = PageRequest.of(page-1,10);
        model.addAttribute("courses", coursesService.getAllCourses(pageable));
        model.addAttribute("tabSize", tabSize);
        model.addAttribute("user", getUserData());
        return "/admin/courses/courses";
    }

    @GetMapping(path = "/courses/addNewCourse")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addNewCoursePage(){
        return "/admin/courses/addNewCourse";
    }

    @PostMapping(path = "/courses/addCourse")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCourse(@RequestParam(name = "name") String name, @RequestParam(name = "description") String description) {
        Courses course = new Courses(null, name, description, new Date());
        coursesRepository.save(course);
        return "redirect:/admin/courses/courses";
    }

    @GetMapping(path = "/courses/courseDetails/{id}")
    @PreAuthorize(("hasRole('ROLE_ADMIN')"))
    public String getCourseDetails(Model model, @PathVariable(name = "id") Long id){
        Courses course = coursesService.getOneById(id);
        model.addAttribute("course", course);
        return "/admin/courses/courseDetails";
    }

    @PostMapping(path = "/courses/editCourse")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editCourse(@RequestParam(name = "id") Long id, @RequestParam(name = "name") String name,
                             @RequestParam(name = "description") String description){
        Courses courses = coursesService.getOneById(id);
        courses.setName(name);
        courses.setDescription(description);
        coursesRepository.save(courses);
        return "redirect:/admin/courses/courses";
    }

    @PostMapping(path = "/courses/deleteCourse")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteCourse(@RequestParam(name = "id") Long id){

        coursesService.deleteCourse(coursesService.getOneById(id));
        return "redirect:/admin/courses/courses";
    }









    protected Long globalCourseId;
                                            //Chapters
    @GetMapping(path = "/chapters/chapters/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String chapters(@PathVariable(name = "id") Long id, Model model, @RequestParam(name = "page", defaultValue = "1") int page){
        globalCourseId = id;
        int count = userRepository.countAllByDeletedAtNull();
        int tabSize = (count+9)/10;
        if(page < 1){
            page = 1;
        }
        Pageable pageable = PageRequest.of(page-1,10);
        model.addAttribute("chapters", chaptersService.getAllChapters(coursesService.getOneById(globalCourseId), pageable));
        model.addAttribute("tabSize", tabSize);
        model.addAttribute("user", getUserData());
        model.addAttribute("course", coursesService.getOneById(id));
        return "/admin/chapters/chapters";
    }

    @GetMapping(path = "/chapters/addNewChapter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addNewChapterPage(Model model, @RequestParam(name = "error", required = false) String error){
        model.addAttribute("error", error);
        int k = 0, l = 0;
        int [] array = new int[30], arrayOfOrders = new int[30-chaptersRepository.countAllByCourse(coursesService.getOneById(globalCourseId))];
        for(int i = 0; i < 30; i ++){
            array[i] = i+1;
        }
        List<Chapters> chaptersList = chaptersService.getAllChaptersToDeleteOrders(coursesService.getOneById(globalCourseId));
        for(int i = 0; i < chaptersList.size(); i ++){
            array[chaptersList.get(i).getOrder()-1] = 0;
        }
        for(int i = 0; i < 30; i++){
            if(array[i] != 0){
                arrayOfOrders[k] = array[i];
                k++;
            }
        }
        model.addAttribute("orders", arrayOfOrders);
        model.addAttribute("course", coursesService.getOneById(globalCourseId));
        return "/admin/chapters/addNewChapter";
    }

    @PostMapping(path = "/chapters/addChapter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addChapter(@RequestParam(name = "name") String name, @RequestParam(name = "description") String description,
                             @RequestParam(name = "order") int order){
        Courses course = coursesService.getOneById(globalCourseId);
        if(chaptersService.getByCourseAndOrder(course, order) == null){
            Chapters chapter = new Chapters(null, name, description, new Date(), course, order);
            chaptersRepository.save(chapter);
            return "redirect:/admin/chapters/chapters/"+globalCourseId;
        }
        return "redirect:/admin/chapters/addNewChapter?error";
    }

    @GetMapping(path = "/chapters/chapterDetails/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String chapterDetails(Model model, @PathVariable(name = "id") Long id){
        int k = 0;
        int [] array = new int[30], arrayOfOrders = new int[30-chaptersRepository.countAllByCourse(coursesService.getOneById(globalCourseId))+1];
        for(int i = 0; i < 30; i ++){
            array[i] = i+1;
        }
        List<Chapters> chaptersList = chaptersService.getAllChaptersToDeleteOrders(coursesService.getOneById(globalCourseId));
        for(int i = 0; i < chaptersList.size(); i ++){
            if(id != chaptersList.get(i).getId()){
                array[chaptersList.get(i).getOrder()-1] = 0;
            }
        }
        for(int i = 0; i < 30; i++){
            if(array[i] != 0){
                arrayOfOrders[k] = array[i];
                k++;
            }
        }
        model.addAttribute("orders", arrayOfOrders);
        model.addAttribute("course", coursesService.getOneById(globalCourseId));
        model.addAttribute("chapter", chaptersService.getChapterById(id));
        return "/admin/chapters/chapterDetails";
    }

    @PostMapping(path = "/chapters/editChapter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editChapter(@RequestParam(name = "name") String name, @RequestParam(name = "description") String description,
                              @RequestParam(name = "order", required = false) int order, @RequestParam(name = "id") Long id){
        Chapters chapter = chaptersService.getChapterById(id);
        chapter.setName(name);
        chapter.setDescription(description);
        if(order == 0){
            order = chapter.getOrder();
        }
        chapter.setOrder(order);
        chaptersRepository.save(chapter);
        return "redirect:/admin/chapters/chapters/"+globalCourseId;
    }

    @PostMapping(path = "/chapters/deleteChapter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteChapter(@RequestParam(name = "id") Long id){
        chaptersService.deleteChapter(chaptersService.getChapterById(id));
        return "redirect:/admin/chapters/chapters/"+globalCourseId;
    }










    protected Long globalChapterId;
                                                        //Lessons
    @GetMapping(path = "/lessons/lessons/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String lessons(Model model, @PathVariable(name = "id") Long id, @RequestParam(name = "page", defaultValue = "1") int page){
        globalChapterId = id;

        int count = userRepository.countAllByDeletedAtNull();
        int tabSize = (count+9)/10;
        if(page < 1){
            page = 1;
        }
        Pageable pageable = PageRequest.of(page-1,10);

        Chapters chapter = chaptersService.getChapterById(globalChapterId);
        model.addAttribute("course", coursesService.getOneById(globalCourseId));
        model.addAttribute("chapter", chapter);
        model.addAttribute("lessons", lessonsService.getAllLessons(chapter, pageable));
        model.addAttribute("tabSize", tabSize);
        return "/admin/lessons/lessons";
    }

    @GetMapping(path = "/lessons/addNewLesson")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addNewLesson(Model model, @RequestParam(name = "error", required = false) String error){
        model.addAttribute("error", error);

        int k = 0;
        int [] array = new int[30], arrayOfOrders = new int[30-lessonsRepository.countAllByChapter(chaptersService.getChapterById(globalChapterId))];
        for(int i = 0; i < 30; i ++){
            array[i] = i+1;
        }
        List<Lesson> lessonsList = lessonsService.getAllLessonsToDeleteOrders(chaptersService.getChapterById(globalChapterId));
        for(int i = 0; i < lessonsList.size(); i ++){
            array[lessonsList.get(i).getOrder()-1] = 0;
        }
        for(int i = 0; i < 30; i++){
            if(array[i] != 0){
                arrayOfOrders[k] = array[i];
                k++;
            }
        }
        model.addAttribute("orders", arrayOfOrders);
        model.addAttribute("course", coursesService.getOneById(globalCourseId));
        model.addAttribute("chapter", chaptersService.getChapterById(globalChapterId));
        return "/admin/lessons/addNewLesson";
    }

    @PostMapping(path = "/lessons/addLesson")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addLesson(@RequestParam(name = "name") String name, @RequestParam(name = "content") String content,
                            @RequestParam(name = "type") int type, @RequestParam(name = "order") int order){
        Lesson lesson = lessonsService.getOneByChapterAndOrder(chaptersService.getChapterById(globalChapterId), order);
        if(lesson == null){
            Lesson newLesson = new Lesson(null, type, name, content, new Date(), chaptersService.getChapterById(globalChapterId), order);
            lessonsRepository.save(newLesson);
            return "redirect:/admin/lessons/lessons/"+globalChapterId;
        }
        return "redirect:/admin/lessons/addNewLesson?error";
    }

    @GetMapping(path = "/lessons/lessonDetails/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getLessonDetails(Model model, @PathVariable(name = "id") Long id){
        model.addAttribute(lessonsService.getLessonById(id));
        int k = 0;
        int [] array = new int[30], arrayOfOrders = new int[30-lessonsRepository.countAllByChapter(chaptersService.getChapterById(globalChapterId))+1];
        for(int i = 0; i < 30; i ++){
            array[i] = i+1;
        }
        List<Lesson> lessonsList = lessonsService.getAllLessonsToDeleteOrders(chaptersService.getChapterById(globalChapterId));
        for(int i = 0; i < lessonsList.size(); i ++){
            if(lessonsList.get(i).getId() != id){
                array[lessonsList.get(i).getOrder()-1] = 0;
            }
        }
        for(int i = 0; i < 30; i++){
            if(array[i] != 0){
                arrayOfOrders[k] = array[i];
                k++;
            }
        }
        model.addAttribute("orders", arrayOfOrders);
        model.addAttribute("course", coursesService.getOneById(globalCourseId));
        model.addAttribute("chapter", chaptersService.getChapterById(globalChapterId));
        model.addAttribute("lesson", lessonsService.getLessonById(id));
        return "/admin/lessons/lessonDetails";
    }

    @PostMapping(path = "/lessons/editLesson")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editLesson(Model model, @RequestParam(name = "id") Long id, @RequestParam(name = "name") String name,
                             @RequestParam(name = "content") String content, @RequestParam(name = "type") int type,
                             @RequestParam(name = "order") int order){
        Lesson lesson = lessonsService.getLessonById(id);
        lesson.setName(name);
        lesson.setContent(content);
        lesson.setType(type);
        lesson.setOrder(order);
        return "redirect:/admin/lessons/lessons/"+globalChapterId;
    }

    @PostMapping(path = "/lessons/deleteLesson")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteLesson(@RequestParam(name = "id") Long id){
        lessonsRepository.delete(lessonsService.getLessonById(id));
        return "redirect:/admin/lessons/lessons/"+globalChapterId;
    }
}
